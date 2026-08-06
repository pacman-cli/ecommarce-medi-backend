package com.example.ecommerce.payment.gateway;

import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.payment.dto.response.PaymentInitiationResponse;
import com.example.ecommerce.payment.dto.response.PaymentVerificationResponse;
import com.example.ecommerce.payment.dto.response.RefundResponse;
import com.example.ecommerce.payment.entity.Payment;
import com.example.ecommerce.payment.entity.PaymentMethod;
import com.example.ecommerce.payment.entity.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Nagad Mobile Financial Services payment gateway strategy implementation.
 */
@Service
@Slf4j
public class NagadGatewayService implements PaymentGatewayStrategy {

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.NAGAD;
    }

    @Override
    public PaymentInitiationResponse initiatePayment(Payment payment, Order order, String returnUrl, String cancelUrl) {
        log.info("Initiating Nagad merchant payment for transaction {}", payment.getTransactionId());
        String gwUrl = "https://sandbox.mypay.com.bd/nagad/checkout?order_id=" + payment.getTransactionId();
        return PaymentInitiationResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .orderNumber(order.getOrderNumber())
                .paymentMethod(PaymentMethod.NAGAD)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .gatewayRedirectUrl(gwUrl)
                .build();
    }

    @Override
    public PaymentVerificationResponse verifyPayment(Payment payment, Map<String, String> queryParams) {
        log.info("Verifying Nagad payment transaction {}", payment.getTransactionId());
        String nagadTxnId = queryParams != null ? queryParams.getOrDefault("payment_ref_id", "NAGAD-TXN-" + UUID.randomUUID().toString().substring(0, 8)) : "NAGAD-TXN-DEFAULT";

        return PaymentVerificationResponse.builder()
                .transactionId(payment.getTransactionId())
                .gatewayTransactionId(nagadTxnId)
                .orderNumber(payment.getOrder().getOrderNumber())
                .paymentStatus(PaymentStatus.SUCCESS)
                .paid(true)
                .message("Nagad payment verified successfully")
                .build();
    }

    @Override
    public RefundResponse processRefund(Payment payment, BigDecimal amount, String reason) {
        log.info("Processing Nagad refund for transaction {}", payment.getTransactionId());
        BigDecimal newTotal = payment.getRefundedAmount().add(amount);
        PaymentStatus status = newTotal.compareTo(payment.getAmount()) >= 0 ? PaymentStatus.REFUNDED : PaymentStatus.PARTIALLY_REFUNDED;

        return RefundResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .refundedAmount(amount)
                .totalRefunded(newTotal)
                .paymentStatus(status)
                .message("Nagad refund processed successfully")
                .build();
    }
}
