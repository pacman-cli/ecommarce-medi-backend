package com.example.ecommerce.delivery.dto.enums;

/**
 * Shipment lifecycle fulfillment states.
 */
public enum DeliveryStatus {
    UNASSIGNED,
    ASSIGNED,
    PICKED_UP,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    FAILED_ATTEMPT,
    RETURNED
}
