package com.example.ecommerce.cart.entity;

/**
 * Operational lifecycle status of a shopping cart.
 */
public enum CartStatus {
    ACTIVE,
    MERGED,
    CONVERTED_TO_ORDER,
    ABANDONED
}
