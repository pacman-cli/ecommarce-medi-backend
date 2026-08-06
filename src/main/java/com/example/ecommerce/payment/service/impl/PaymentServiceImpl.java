package com.example.ecommerce.payment.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.payment.dto.request.InitiatePaymentRequest;
import com.example.ecommerce.payment.dto.request.PaymentFilterRequest;
import com.example.ecommerce.payment.dto.request.RefundPaymentRequest;
import com.example.ecommerce.payment.dto.request.VerifyPaymentRequest;
import com.example.ecommerce.payment.dto.response.PaymentInitiationResponse;
import com.example.ecommerce.payment.dto.response.PaymentResponse;
import com.example.ecommerce.payment.dto.response.PaymentVerificationResponse;
import com.example.ecommerce.payment.dto.response.RefundResponse;
import com.example.ecommerce.payment.entity.Payment;
import com.example.ecommerce.payment.entity.PaymentMethod;
import com.example.ecommerce.payment.entity.PaymentStatus;
import com.example.ecommerce.payment.entity.TransactionType;
import com.example.ecommerce.payment.gateway.PaymentGatewayStrategy;
import com.example.ecommerce.payment.mapper.PaymentMapper;
import com.example.ecommerce.payment.repository.PaymentRepository;
import com.example.ecommerce.payment.service.PaymentService;
import com.example.ecommerce.payment.specification.PaymentSpecification;
import com.example.ecommerce.payment.validator.PaymentValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Service implementation managing multi-gateway online payments (SSLCommerz, bKash, Nagad, COD),
 * verification callbacks, IPN webhooks, audit transaction logging and refunds.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentValidator paymentValidator;
    private final Map<PaymentMethod, PaymentGatewayStrategy> gatewayStrategies;

    private static final Random RANDOM = new Random();

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              PaymentMapper paymentMapper,
                              PaymentValidator paymentValidator,
                              List<PaymentGatewayStrategy> strategies) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
        this.paymentValidator = paymentValidator;
        this.gatewayStrategies = new EnumMap<>(PaymentMethod.class);
        for (PaymentGatewayStrategy strategy : strategies) {
            this.gatewayStrategies.put(strategy.getSupportedMethod(), strategy);
        }
    }

    @Override
    @Transactional
    public PaymentInitiationResponse initiatePayment(InitiatePaymentRequest request) {
        log.info("Initiating payment for order ID {} using method {}", request.getOrderId(), request.getPaymentMethod());

        Order order = orderRepository.findByIdAndDeletedFalse(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.getOrderId()));

        paymentValidator.validateOrderForPayment(order);

        PaymentGatewayStrategy strategy = getStrategy(request.getPaymentMethod());

        Optional<Payment> existingOpt = paymentRepository.findByOrderIdAndDeletedFalse(order.getId());
        Payment payment;
        if (existingOpt.isPresent()) {
            payment = existingOpt.get();
            payment.setPaymentMethod(request.getPaymentMethod());
        } else {
            String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String suffix = String.format("%05d", RANDOM.nextInt(100000));
            String txnId = "TXN-" + datePrefix + "-" + suffix;

            payment = Payment.builder()
                    .transactionId(txnId)
                    .order(order)
                    .paymentMethod(request.getPaymentMethod())
                    .amount(order.getGrandTotal())
                    .currency("BDT")
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();
        }

        PaymentInitiationResponse initiationResponse = strategy.initiatePayment(payment, order, request.getReturnUrl(), request.getCancelUrl());

        payment.setRedirectUrl(initiationResponse.getGatewayRedirectUrl());
        payment.addTransaction(TransactionType.INITIATE, request.getPaymentMethod().name(), null, request.toString(), initiationResponse.toString(), PaymentStatus.PENDING);

        Payment saved = paymentRepository.save(payment);
        log.info("Successfully initiated payment with transaction ID {}", saved.getTransactionId());

        return initiationResponse;
    }

    @Override
    @Transactional
    public PaymentVerificationResponse verifyPayment(VerifyPaymentRequest request, Map<String, String> queryParams) {
        log.info("Verifying payment transaction ID {}", request.getTransactionId());

        Payment payment = paymentRepository.findByTransactionIdAndDeletedFalse(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment transaction not found: " + request.getTransactionId()));

        PaymentGatewayStrategy strategy = getStrategy(payment.getPaymentMethod());
        PaymentVerificationResponse verificationResponse = strategy.verifyPayment(payment, queryParams);

        updatePaymentAndOrderStatus(payment, verificationResponse, TransactionType.VERIFY, queryParams != null ? queryParams.toString() : "");

        return verificationResponse;
    }

    @Override
    @Transactional
    public PaymentVerificationResponse processWebhook(String gateway, Map<String, String> payload) {
        log.info("Processing IPN webhook from gateway '{}'", gateway);
        String txnId = extractTransactionId(payload);

        if (!StringUtils.hasText(txnId)) {
            throw new BadRequestException("Could not extract transaction ID from webhook payload");
        }

        Payment payment = paymentRepository.findByTransactionIdAndDeletedFalse(txnId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment transaction not found for webhook: " + txnId));

        PaymentGatewayStrategy strategy = getStrategy(payment.getPaymentMethod());
        PaymentVerificationResponse verificationResponse = strategy.verifyPayment(payment, payload);

        updatePaymentAndOrderStatus(payment, verificationResponse, TransactionType.WEBHOOK, payload.toString());

        return verificationResponse;
    }

    @Override
    @Transactional
    public RefundResponse refundPayment(RefundPaymentRequest request) {
        log.info("Processing refund for payment ID {}, amount ${}", request.getPaymentId(), request.getAmount());

        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + request.getPaymentId()));

        paymentValidator.validateRefund(payment, request.getAmount());

        PaymentGatewayStrategy strategy = getStrategy(payment.getPaymentMethod());
        RefundResponse refundResponse = strategy.processRefund(payment, request.getAmount(), request.getReason());

        payment.setRefundedAmount(refundResponse.getTotalRefunded());
        payment.setPaymentStatus(refundResponse.getPaymentStatus());
        payment.setRefundedAt(Instant.now());

        payment.addTransaction(TransactionType.REFUND, payment.getPaymentMethod().name(), null, request.toString(), refundResponse.toString(), refundResponse.getPaymentStatus());

        Order order = payment.getOrder();
        if (order != null && refundResponse.getPaymentStatus() == PaymentStatus.REFUNDED) {
            order.setPaymentStatus(com.example.ecommerce.order.entity.PaymentStatus.REFUNDED);
            order.setStatus(OrderStatus.REFUNDED);
            order.addTimeline(OrderStatus.REFUNDED, "Payment refunded: " + request.getReason(), "System");
            orderRepository.save(order);
        }

        paymentRepository.save(payment);
        log.info("Successfully processed refund for payment ID {}", payment.getId());
        return refundResponse;
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionIdAndDeletedFalse(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with transaction ID: " + transactionId));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PageResponse<PaymentResponse> getPayments(PaymentFilterRequest filter, Pageable pageable) {
        Specification<Payment> spec = PaymentSpecification.build(filter);
        Page<Payment> page = paymentRepository.findAll(spec, pageable);
        return PageResponse.from(page, paymentMapper::toResponse);
    }

    private PaymentGatewayStrategy getStrategy(PaymentMethod method) {
        PaymentGatewayStrategy strategy = gatewayStrategies.get(method);
        if (strategy == null) {
            throw new BadRequestException("Unsupported payment method: " + method);
        }
        return strategy;
    }

    private void updatePaymentAndOrderStatus(Payment payment, PaymentVerificationResponse response, TransactionType txnType, String rawPayload) {
        if (response.isPaid()) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(Instant.now());
            if (StringUtils.hasText(response.getGatewayTransactionId())) {
                payment.setGatewayTransactionId(response.getGatewayTransactionId());
            }

            Order order = payment.getOrder();
            if (order != null) {
                order.setPaymentStatus(com.example.ecommerce.order.entity.PaymentStatus.PAID);
                if (order.getStatus() == OrderStatus.PENDING) {
                    order.setStatus(OrderStatus.CONFIRMED);
                    order.addTimeline(OrderStatus.CONFIRMED, "Payment confirmed via " + payment.getPaymentMethod(), "System Gateway");
                }
                orderRepository.save(order);
            }
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setFailureReason(response.getMessage());
        }

        payment.addTransaction(txnType, payment.getPaymentMethod().name(), response.getGatewayTransactionId(), rawPayload, response.toString(), payment.getPaymentStatus());
        paymentRepository.save(payment);
    }

    private String extractTransactionId(Map<String, String> payload) {
        if (payload == null) return null;
        if (payload.containsKey("tran_id")) return payload.get("tran_id");
        if (payload.containsKey("paymentID")) return payload.get("paymentID");
        if (payload.containsKey("order_id")) return payload.get("order_id");
        if (payload.containsKey("transactionId")) return payload.get("transactionId");
        return null;
    }
}
