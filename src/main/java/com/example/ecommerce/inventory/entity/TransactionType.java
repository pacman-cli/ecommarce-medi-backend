package com.example.ecommerce.inventory.entity;

/**
 * Types of inventory transactions representing stock movements and adjustments.
 */
public enum TransactionType {
    INBOUND_PURCHASE,
    OUTBOUND_SALE,
    ADJUSTMENT_INCREASE,
    ADJUSTMENT_DECREASE,
    RETURN_CUSTOMER,
    RETURN_SUPPLIER,
    TRANSFER,
    EXPIRED_DISCARD
}
