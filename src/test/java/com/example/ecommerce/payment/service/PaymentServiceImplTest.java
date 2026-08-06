package com.example.ecommerce.payment.service;

import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.payment.dto.request.InitiatePaymentRequest;
import com.example.ecommerce.payment.dto.request.RefundPaymentRequest;
import com.example.ecommerce.payment.dto.request.VerifyPaymentRequest;
import com.example.ecommerce.payment.dto.response.PaymentInitiationResponse;
import com.example.ecommerce.payment.dto.response.PaymentVerificationResponse;
import com.example.ecommerce.payment.dto.response.RefundResponse;
import com.example.ecommerce.payment.entity.Payment;
import com.example.ecommerce.payment.entity.PaymentMethod;
import com.example.ecommerce.payment.entity.PaymentStatus;
import com.example.ecommerce.payment.gateway.CodGatewayService;
import com.example.ecommerce.payment.gateway.PaymentGatewayStrategy;
import com.example.ecommerce.payment.gateway.SslCommerzGatewayService;
import com.example.ecommerce.payment.mapper.PaymentMapper;
import com.example.ecommerce.payment.repository.PaymentRepository;
import com.example.ecommerce.payment.service.impl.PaymentServiceImpl;
import com.example.ecommerce.payment.validator.PaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentValidator paymentValidator;

    private PaymentServiceImpl paymentService;

    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        SslCommerzGatewayService sslCommerz = new SslCommerzGatewayService();
        CodGatewayService cod = new CodGatewayService();
        List<PaymentGatewayStrategy> strategies = List.of(sslCommerz, cod);

        paymentService = new PaymentServiceImpl(paymentRepository, orderRepository, paymentMapper, paymentValidator, strategies);

        order = Order.builder()
                .orderNumber("ORD-20260804-00001")
                .invoiceNumber("INV-20260804-00001")
                .grandTotal(new BigDecimal("94.50"))
                .status(OrderStatus.PENDING)
                .paymentStatus(com.example.ecommerce.order.entity.PaymentStatus.PENDING)
                .build();
        order.setId(100L);

        payment = Payment.builder()
                .transactionId("TXN-20260804-00001")
                .order(order)
                .paymentMethod(PaymentMethod.SSLCOMMERZ)
                .amount(new BigDecimal("94.50"))
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        payment.setId(50L);
    }

    @Test
    @DisplayName("initiatePayment should validate order, invoke gateway strategy, save payment and return initiation DTO")
    void initiatePayment_Success() {
        InitiatePaymentRequest initReq = InitiatePaymentRequest.builder()
                .orderId(100L)
                .paymentMethod(PaymentMethod.SSLCOMMERZ)
                .build();

        when(orderRepository.findByIdAndDeletedFalse(100L)).thenReturn(Optional.of(order));
        doNothing().when(paymentValidator).validateOrderForPayment(order);
        when(paymentRepository.findByOrderIdAndDeletedFalse(100L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentInitiationResponse response = paymentService.initiatePayment(initReq);

        assertThat(response).isNotNull();
        assertThat(response.getPaymentMethod()).isEqualTo(PaymentMethod.SSLCOMMERZ);
        assertThat(response.getGatewayRedirectUrl()).contains("sslcommerz.com");

        verify(paymentValidator).validateOrderForPayment(order);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("verifyPayment should invoke strategy, update payment and order status to SUCCESS & PAID")
    void verifyPayment_Success() {
        VerifyPaymentRequest verifyReq = VerifyPaymentRequest.builder()
                .transactionId("TXN-20260804-00001")
                .valId("SSL-VAL-12345")
                .build();

        when(paymentRepository.findByTransactionIdAndDeletedFalse("TXN-20260804-00001")).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentVerificationResponse response = paymentService.verifyPayment(verifyReq, Map.of("val_id", "SSL-VAL-12345", "status", "VALID"));

        assertThat(response).isNotNull();
        assertThat(response.isPaid()).isTrue();
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(order.getPaymentStatus()).isEqualTo(com.example.ecommerce.order.entity.PaymentStatus.PAID);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    @DisplayName("refundPayment should validate, process refund with gateway and update payment status")
    void refundPayment_Success() {
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        RefundPaymentRequest refundReq = RefundPaymentRequest.builder()
                .paymentId(50L)
                .amount(new BigDecimal("94.50"))
                .reason("Defective product")
                .build();

        when(paymentRepository.findById(50L)).thenReturn(Optional.of(payment));
        doNothing().when(paymentValidator).validateRefund(eq(payment), eq(new BigDecimal("94.50")));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        RefundResponse response = paymentService.refundPayment(refundReq);

        assertThat(response).isNotNull();
        assertThat(response.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.REFUNDED);
    }
}
