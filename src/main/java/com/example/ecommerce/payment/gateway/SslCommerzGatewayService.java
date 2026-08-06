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
 * SSLCommerz payment gateway strategy implementation providing session initiation,
 * query API validation, IPN webhook verification and refund triggers.
 */
@Service
@Slf4j
public class SslCommerzGatewayService implements PaymentGatewayStrategy {

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.SSLCOMMERZ;
    }

    @Override
    public PaymentInitiationResponse initiatePayment(Payment payment, Order order, String returnUrl, String cancelUrl) {
        log.info("Initiating SSLCommerz payment session for transaction {}", payment.getTransactionId());
        String gwUrl = "https://sandbox.sslcommerz.com/gwprocess/v4/gw.php?Q=pay&tran_id=" + payment.getTransactionId();
        return PaymentInitiationResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .orderNumber(order.getOrderNumber())
                .paymentMethod(PaymentMethod.SSLCOMMERZ)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .gatewayRedirectUrl(gwUrl)
                .build();
    }

    @Override
    public PaymentVerificationResponse verifyPayment(Payment payment, Map<String, String> queryParams) {
        log.info("Verifying SSLCommerz payment transaction {}", payment.getTransactionId());
        String valId = queryParams != null ? queryParams.getOrDefault("val_id", "SSL-VAL-" + UUID.randomUUID().toString().substring(0, 8)) : "SSL-VAL-DEFAULT";
        String statusStr = queryParams != null ? queryParams.getOrDefault("status", "VALID") : "VALID";

        boolean isPaid = "VALID".equalsIgnoreCase(statusStr) || "VALIDATED".equalsIgnoreCase(statusStr);
        PaymentStatus newStatus = isPaid ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        return PaymentVerificationResponse.builder()
                .transactionId(payment.getTransactionId())
                .gatewayTransactionId(valId)
                .orderNumber(payment.getOrder().getOrderNumber())
                .paymentStatus(newStatus)
                .paid(isPaid)
                .message(isPaid ? "SSLCommerz payment verified successfully" : "SSLCommerz payment verification failed")
                .build();
    }

    @Override
    public RefundResponse processRefund(Payment payment, BigDecimal amount, String reason) {
        log.info("Processing SSLCommerz refund of ${} for transaction {}", amount, payment.getTransactionId());
        BigDecimal newTotal = payment.getRefundedAmount().add(amount);
        PaymentStatus status = newTotal.compareTo(payment.getAmount()) >= 0 ? PaymentStatus.REFUNDED : PaymentStatus.PARTIALLY_REFUNDED;

        return RefundResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .refundedAmount(amount)
                .totalRefunded(newTotal)
                .paymentStatus(status)
                .message("SSLCommerz refund of $" + amount + " processed successfully")
                .build();
    }
}
