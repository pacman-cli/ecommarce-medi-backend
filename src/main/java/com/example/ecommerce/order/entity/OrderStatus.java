package com.example.ecommerce.order.entity;

/**
 * Order status lifecycle states.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PACKED,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED,
    REFUNDED
}
