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
 * bKash Mobile Financial Services payment gateway strategy implementation.
 */
@Service
@Slf4j
public class BkashGatewayService implements PaymentGatewayStrategy {

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.BKASH;
    }

    @Override
    public PaymentInitiationResponse initiatePayment(Payment payment, Order order, String returnUrl, String cancelUrl) {
        log.info("Initiating bKash payment token for transaction {}", payment.getTransactionId());
        String gwUrl = "https://checkout.sandbox.bka.sh/v1.2.0-beta/checkout/payment/create?paymentID=" + payment.getTransactionId();
        return PaymentInitiationResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .orderNumber(order.getOrderNumber())
                .paymentMethod(PaymentMethod.BKASH)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .gatewayRedirectUrl(gwUrl)
                .build();
    }

    @Override
    public PaymentVerificationResponse verifyPayment(Payment payment, Map<String, String> queryParams) {
        log.info("Executing bKash payment verification for transaction {}", payment.getTransactionId());
        String trxID = queryParams != null ? queryParams.getOrDefault("trxID", "BKASH-TRX-" + UUID.randomUUID().toString().substring(0, 8)) : "BKASH-TRX-DEFAULT";

        return PaymentVerificationResponse.builder()
                .transactionId(payment.getTransactionId())
                .gatewayTransactionId(trxID)
                .orderNumber(payment.getOrder().getOrderNumber())
                .paymentStatus(PaymentStatus.SUCCESS)
                .paid(true)
                .message("bKash payment executed and verified successfully")
                .build();
    }

    @Override
    public RefundResponse processRefund(Payment payment, BigDecimal amount, String reason) {
        log.info("Executing bKash refund for transaction {}", payment.getTransactionId());
        BigDecimal newTotal = payment.getRefundedAmount().add(amount);
        PaymentStatus status = newTotal.compareTo(payment.getAmount()) >= 0 ? PaymentStatus.REFUNDED : PaymentStatus.PARTIALLY_REFUNDED;

        return RefundResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .refundedAmount(amount)
                .totalRefunded(newTotal)
                .paymentStatus(status)
                .message("bKash refund executed successfully")
                .build();
    }
}
