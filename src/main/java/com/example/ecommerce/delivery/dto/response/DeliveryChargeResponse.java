package com.example.ecommerce.delivery.dto.response;

import com.example.ecommerce.delivery.dto.enums.ShippingMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO representing computed delivery fee rates, surcharges, and estimated delivery dates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Calculated delivery charge and estimated arrival payload")
public class DeliveryChargeResponse {

    @Schema(description = "Delivery zone name", example = "Inside Dhaka City")
    private String zoneName;

    @Schema(description = "Selected shipping method", example = "EXPRESS")
    private ShippingMethod shippingMethod;

    @Schema(description = "Base shipping charge", example = "60.00")
    private BigDecimal baseCharge;

    @Schema(description = "Express priority surcharge fee", example = "60.00")
    private BigDecimal expressSurcharge;

    @Schema(description = "Cash On Delivery handling fee", example = "15.00")
    private BigDecimal codFee;

    @Schema(description = "Total delivery charge amount", example = "135.00")
    private BigDecimal totalDeliveryCharge;

    @Schema(description = "Minimum estimated delivery date", example = "2026-08-06")
    private LocalDate estimatedMinDate;

    @Schema(description = "Maximum estimated delivery date", example = "2026-08-07")
    private LocalDate estimatedMaxDate;

    @Schema(description = "Formatted delivery date range estimate label", example = "Aug 06 - Aug 07, 2026")
    private String deliveryEstimateLabel;
}
