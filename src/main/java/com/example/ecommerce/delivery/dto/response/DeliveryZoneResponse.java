package com.example.ecommerce.delivery.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO representing a delivery geographic zone with base rates and delivery estimates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delivery zone rates and location details")
public class DeliveryZoneResponse {

    @Schema(description = "Zone ID", example = "1")
    private Long id;

    @Schema(description = "Zone name", example = "Inside Dhaka City")
    private String name;

    @Schema(description = "Zone code", example = "INSIDE_DHAKA")
    private String code;

    @Schema(description = "Division name", example = "Dhaka")
    private String division;

    @Schema(description = "District name", example = "Dhaka")
    private String district;

    @Schema(description = "Standard base delivery fee", example = "60.00")
    private BigDecimal baseFee;

    @Schema(description = "Express delivery fee surcharge", example = "120.00")
    private BigDecimal expressFee;

    @Schema(description = "COD handling fee", example = "15.00")
    private BigDecimal codFee;

    @Schema(description = "Minimum delivery days", example = "1")
    private Integer minDeliveryDays;

    @Schema(description = "Maximum delivery days", example = "2")
    private Integer maxDeliveryDays;

    @Schema(description = "Is zone active", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp", example = "2026-08-05T14:00:00Z")
    private Instant createdAt;
}
