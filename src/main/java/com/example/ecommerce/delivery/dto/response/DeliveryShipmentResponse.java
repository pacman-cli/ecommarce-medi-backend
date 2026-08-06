package com.example.ecommerce.delivery.dto.response;

import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
import com.example.ecommerce.delivery.dto.enums.ShippingMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Full shipment details response payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Shipment details payload")
public class DeliveryShipmentResponse {

    @Schema(description = "Shipment ID", example = "101")
    private Long id;

    @Schema(description = "Unique shipment code", example = "SHP-20260805-00101")
    private String shipmentNumber;

    @Schema(description = "Logistics tracking number code", example = "TRK-20260805-98421")
    private String trackingNumber;

    @Schema(description = "Associated Order ID", example = "501")
    private Long orderId;

    @Schema(description = "Associated Order Number", example = "ORD-20260804-98421")
    private String orderNumber;

    @Schema(description = "Logistics partner carrier details")
    private DeliveryPartnerResponse partner;

    @Schema(description = "Delivery zone details")
    private DeliveryZoneResponse zone;

    @Schema(description = "Shipping method", example = "STANDARD")
    private ShippingMethod shippingMethod;

    @Schema(description = "Fulfillment status", example = "OUT_FOR_DELIVERY")
    private DeliveryStatus status;

    @Schema(description = "Recipient name", example = "Jane Doe")
    private String recipientName;

    @Schema(description = "Recipient phone", example = "+8801700000000")
    private String recipientPhone;

    @Schema(description = "Shipping address text", example = "House 12, Road 5, Dhanmondi, Dhaka")
    private String shippingAddress;

    @Schema(description = "Assigned rider name", example = "Rahim Uddin")
    private String riderName;

    @Schema(description = "Assigned rider phone", example = "+8801711223344")
    private String riderPhone;

    @Schema(description = "Vehicle description", example = "Motorbike (Dhaka Metro-HA-1234)")
    private String vehicleInfo;

    @Schema(description = "Is Cash On Delivery", example = "true")
    private boolean isCod;

    @Schema(description = "COD amount to be collected", example = "1450.00")
    private BigDecimal codAmount;

    @Schema(description = "COD handling fee", example = "15.00")
    private BigDecimal codFee;

    @Schema(description = "Delivery fee", example = "60.00")
    private BigDecimal deliveryFee;

    @Schema(description = "Scheduled delivery date", example = "2026-08-10")
    private LocalDate scheduledDate;

    @Schema(description = "Scheduled time window", example = "10:00 AM - 01:00 PM")
    private String scheduledTimeSlot;

    @Schema(description = "Estimated delivery date", example = "2026-08-07")
    private LocalDate estimatedDeliveryDate;

    @Schema(description = "Delivery completion timestamp", example = "2026-08-07T11:45:00Z")
    private Instant deliveredAt;

    @Schema(description = "Delivery notes or comments", example = "Fragile medicine - handle with care")
    private String notes;

    @Schema(description = "Shipment lifecycle update timeline checkpoints")
    private List<DeliveryTimelineResponse> timelines;

    @Schema(description = "Shipment creation timestamp", example = "2026-08-05T14:00:00Z")
    private Instant createdAt;
}
