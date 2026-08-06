package com.example.ecommerce.payment.entity;

/**
 * Operational state of a payment transaction.
 */
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    CANCELLED,
    REFUNDED,
    PARTIALLY_REFUNDED
}
