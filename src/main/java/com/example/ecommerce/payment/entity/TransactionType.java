package com.example.ecommerce.payment.entity;

/**
 * Type of gateway interaction logged in payment audit history.
 */
public enum TransactionType {
    INITIATE,
    CALLBACK,
    VERIFY,
    REFUND,
    WEBHOOK
}
