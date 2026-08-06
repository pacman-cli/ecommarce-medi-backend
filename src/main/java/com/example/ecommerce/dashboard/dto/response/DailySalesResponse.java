package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Aggregated daily sales breakdown metric DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Daily sales analytics item")
public class DailySalesResponse {

    @Schema(description = "Calendar date", example = "2026-08-04")
    private LocalDate date;

    @Schema(description = "Formatted date label", example = "Aug 04, 2026")
    private String dateLabel;

    @Schema(description = "Total revenue earned on date", example = "2450.00")
    private BigDecimal totalRevenue;

    @Schema(description = "Total orders placed on date", example = "18")
    private Long orderCount;

    @Schema(description = "Total product units sold on date", example = "64")
    private Long totalItemsSold;

    @Schema(description = "Average order value on date", example = "136.11")
    private BigDecimal averageOrderValue;
}
