package com.example.ecommerce.payment.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.payment.entity.Payment;
import com.example.ecommerce.payment.entity.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Validates payment initiation requirements and refund thresholds.
 */
@Component
public class PaymentValidator {

    public void validateOrderForPayment(Order order) {
        if (order == null || order.isDeleted()) {
            throw new BadRequestException("Order does not exist or has been deleted");
        }
        if (order.getPaymentStatus() == com.example.ecommerce.order.entity.PaymentStatus.PAID) {
            throw new BadRequestException("Order " + order.getOrderNumber() + " is already paid");
        }
    }

    public void validateRefund(Payment payment, BigDecimal refundAmount) {
        if (payment == null || payment.isDeleted()) {
            throw new BadRequestException("Payment record not found");
        }
        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS && payment.getPaymentStatus() != PaymentStatus.PARTIALLY_REFUNDED) {
            throw new BadRequestException("Only successful or partially refunded payments can be refunded");
        }
        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Refund amount must be greater than zero");
        }
        BigDecimal maxRefundable = payment.getAmount().subtract(payment.getRefundedAmount());
        if (refundAmount.compareTo(maxRefundable) > 0) {
            throw new BadRequestException("Refund amount $" + refundAmount + " exceeds remaining refundable balance of $" + maxRefundable);
        }
    }
}
