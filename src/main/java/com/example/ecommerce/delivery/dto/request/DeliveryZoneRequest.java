package com.example.ecommerce.delivery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Payload for creating or updating a delivery zone.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delivery zone creation and update payload")
public class DeliveryZoneRequest {

    @Schema(description = "Zone name", example = "Inside Dhaka City")
    @NotBlank(message = "Zone name is required")
    private String name;

    @Schema(description = "Unique zone code", example = "INSIDE_DHAKA")
    @NotBlank(message = "Zone code is required")
    private String code;

    @Schema(description = "Division name", example = "Dhaka")
    private String division;

    @Schema(description = "District name", example = "Dhaka")
    private String district;

    @Schema(description = "Standard base delivery fee", example = "60.00")
    @NotNull(message = "Base fee is required")
    @DecimalMin(value = "0.0", message = "Base fee cannot be negative")
    private BigDecimal baseFee;

    @Schema(description = "Express delivery fee surcharge", example = "120.00")
    @NotNull(message = "Express fee is required")
    @DecimalMin(value = "0.0", message = "Express fee cannot be negative")
    private BigDecimal expressFee;

    @Schema(description = "COD handling fee", example = "15.00")
    @NotNull(message = "COD fee is required")
    @DecimalMin(value = "0.0", message = "COD fee cannot be negative")
    private BigDecimal codFee;

    @Schema(description = "Minimum estimated delivery days", example = "1")
    @Min(value = 0, message = "Min delivery days cannot be negative")
    private Integer minDeliveryDays;

    @Schema(description = "Maximum estimated delivery days", example = "2")
    @Min(value = 1, message = "Max delivery days must be at least 1")
    private Integer maxDeliveryDays;

    @Schema(description = "Is zone active", example = "true")
    @Builder.Default
    private Boolean active = true;
}
