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

/**
 * Cash on Delivery (COD) payment strategy implementation.
 */
@Service
@Slf4j
public class CodGatewayService implements PaymentGatewayStrategy {

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.COD;
    }

    @Override
    public PaymentInitiationResponse initiatePayment(Payment payment, Order order, String returnUrl, String cancelUrl) {
        log.info("Initiating Cash on Delivery payment for order {}", order.getOrderNumber());
        return PaymentInitiationResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .orderNumber(order.getOrderNumber())
                .paymentMethod(PaymentMethod.COD)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .gatewayRedirectUrl(null)
                .build();
    }

    @Override
    public PaymentVerificationResponse verifyPayment(Payment payment, Map<String, String> queryParams) {
        log.info("Verifying COD payment upon delivery for order {}", payment.getOrder().getOrderNumber());
        return PaymentVerificationResponse.builder()
                .transactionId(payment.getTransactionId())
                .gatewayTransactionId("COD-COLLECTED-" + payment.getTransactionId())
                .orderNumber(payment.getOrder().getOrderNumber())
                .paymentStatus(PaymentStatus.SUCCESS)
                .paid(true)
                .message("Cash on delivery collected and confirmed")
                .build();
    }

    @Override
    public RefundResponse processRefund(Payment payment, BigDecimal amount, String reason) {
        log.info("Processing manual cash refund for COD transaction {}", payment.getTransactionId());
        BigDecimal newTotal = payment.getRefundedAmount().add(amount);
        PaymentStatus status = newTotal.compareTo(payment.getAmount()) >= 0 ? PaymentStatus.REFUNDED : PaymentStatus.PARTIALLY_REFUNDED;

        return RefundResponse.builder()
                .paymentId(payment.getId())
                .transactionId(payment.getTransactionId())
                .refundedAmount(amount)
                .totalRefunded(newTotal)
                .paymentStatus(status)
                .message("Manual COD cash refund recorded successfully")
                .build();
    }
}
