package com.example.ecommerce.delivery.dto.response;

import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
import com.example.ecommerce.delivery.dto.enums.ShippingMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Public customer-facing tracking response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Customer shipment tracking status payload")
public class DeliveryTrackingResponse {

    @Schema(description = "Unique tracking code", example = "TRK-20260805-98421")
    private String trackingNumber;

    @Schema(description = "Order Number", example = "ORD-20260804-98421")
    private String orderNumber;

    @Schema(description = "Current delivery status", example = "OUT_FOR_DELIVERY")
    private DeliveryStatus currentStatus;

    @Schema(description = "Carrier partner name", example = "Steadfast Courier")
    private String partnerName;

    @Schema(description = "Shipping method mode", example = "EXPRESS")
    private ShippingMethod shippingMethod;

    @Schema(description = "Recipient name", example = "Jane Doe")
    private String recipientName;

    @Schema(description = "Delivery destination address", example = "Dhanmondi, Dhaka")
    private String shippingAddress;

    @Schema(description = "Assigned rider name", example = "Rahim Uddin")
    private String riderName;

    @Schema(description = "Assigned rider phone", example = "+8801711223344")
    private String riderPhone;

    @Schema(description = "Estimated delivery date", example = "2026-08-07")
    private LocalDate estimatedDeliveryDate;

    @Schema(description = "Delivered timestamp", example = "2026-08-07T11:45:00Z")
    private Instant deliveredAt;

    @Schema(description = "Full status timeline checkpoints")
    private List<DeliveryTimelineResponse> timelineHistory;
}
