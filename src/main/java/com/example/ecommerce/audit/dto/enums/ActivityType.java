package com.example.ecommerce.audit.dto.enums;

/**
 * Activity log type for tracking user and administrative actions.
 */
public enum ActivityType {
    LOGIN,
    LOGOUT,
    PASSWORD_CHANGE,
    PROFILE_UPDATE,
    ORDER_PLACED,
    ORDER_STATUS_CHANGE,
    PRODUCT_MUTATION,
    PAYMENT_PROCESSING,
    ADMIN_ACTION
}
