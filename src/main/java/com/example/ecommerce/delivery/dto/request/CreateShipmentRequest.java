package com.example.ecommerce.delivery.dto.request;

import com.example.ecommerce.delivery.dto.enums.ShippingMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Payload for initializing a shipment for a placed order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Shipment creation request payload")
public class CreateShipmentRequest {

    @Schema(description = "Order ID", example = "501")
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @Schema(description = "Delivery partner carrier ID", example = "1")
    private Long partnerId;

    @Schema(description = "Delivery zone ID", example = "2")
    private Long zoneId;

    @Schema(description = "Shipping method", example = "EXPRESS")
    @NotNull(message = "Shipping method is required")
    @Builder.Default
    private ShippingMethod shippingMethod = ShippingMethod.STANDARD;

    @Schema(description = "Recipient full name", example = "Jane Doe")
    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @Schema(description = "Recipient phone number", example = "+8801700000000")
    @NotBlank(message = "Recipient phone is required")
    private String recipientPhone;

    @Schema(description = "Full shipping address text", example = "House 12, Road 5, Dhanmondi, Dhaka")
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @Schema(description = "Is Cash On Delivery", example = "true")
    @Builder.Default
    private Boolean isCod = false;

    @Schema(description = "COD amount to be collected", example = "1450.00")
    @DecimalMin(value = "0.0", message = "COD amount cannot be negative")
    private BigDecimal codAmount;

    @Schema(description = "Scheduled delivery date for SCHEDULED shipping method", example = "2026-08-10")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate scheduledDate;

    @Schema(description = "Scheduled time slot string", example = "10:00 AM - 01:00 PM")
    private String scheduledTimeSlot;

    @Schema(description = "Optional delivery notes or instructions", example = "Fragile medicine - handle with care")
    private String notes;
}
