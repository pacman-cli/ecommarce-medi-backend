package com.example.ecommerce.delivery.dto.request;

import com.example.ecommerce.delivery.dto.enums.ShippingMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request payload for calculating delivery charge rate and estimated arrival dates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delivery charge calculation request payload")
public class DeliveryChargeCalculateRequest {

    @Schema(description = "Division or Province name", example = "Dhaka")
    private String division;

    @Schema(description = "District name", example = "Dhaka")
    private String district;

    @Schema(description = "Delivery zone ID (optional if division/district provided)", example = "1")
    private Long zoneId;

    @Schema(description = "Shipping method", example = "EXPRESS")
    @NotNull(message = "Shipping method is required")
    @Builder.Default
    private ShippingMethod shippingMethod = ShippingMethod.STANDARD;

    @Schema(description = "Is Cash On Delivery requested", example = "true")
    @Builder.Default
    private Boolean isCod = false;

    @Schema(description = "Order subtotal amount", example = "1500.00")
    private BigDecimal subtotal;

    @Schema(description = "Package total weight in kilograms", example = "1.5")
    private Double weightKg;
}
