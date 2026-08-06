package com.example.ecommerce.inventory.entity;

/**
 * Operational status of a stock batch.
 */
public enum BatchStatus {
    AVAILABLE,
    LOW_STOCK,
    EXPIRED,
    QUARANTINED,
    DEPLETED
}
