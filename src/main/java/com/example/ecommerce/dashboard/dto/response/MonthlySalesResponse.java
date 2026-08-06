package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Aggregated monthly sales breakdown metric DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Monthly sales summary item")
public class MonthlySalesResponse {

    @Schema(description = "Four-digit calendar year", example = "2026")
    private Integer year;

    @Schema(description = "Numeric month index (1-12)", example = "8")
    private Integer month;

    @Schema(description = "Full month name label", example = "August 2026")
    private String monthName;

    @Schema(description = "Total revenue earned in month", example = "48500.00")
    private BigDecimal totalRevenue;

    @Schema(description = "Total orders placed in month", example = "320")
    private Long orderCount;

    @Schema(description = "Total product units sold in month", example = "1050")
    private Long totalItemsSold;

    @Schema(description = "Average order value for month", example = "151.56")
    private BigDecimal averageOrderValue;
}
