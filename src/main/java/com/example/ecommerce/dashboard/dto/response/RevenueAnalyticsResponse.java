package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Detailed financial revenue analytics breakdown payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Revenue analytics breakdown and financial metric trends")
public class RevenueAnalyticsResponse {

    @Schema(description = "Gross revenue (grand total of completed orders)", example = "68450.00")
    private BigDecimal grossRevenue;

    @Schema(description = "Net revenue (gross revenue minus tax and shipping)", example = "58200.00")
    private BigDecimal netRevenue;

    @Schema(description = "Total sales tax collected", example = "5450.00")
    private BigDecimal totalTax;

    @Schema(description = "Total shipping fee collected", example = "4800.00")
    private BigDecimal totalShipping;

    @Schema(description = "Total discounts granted", example = "2150.00")
    private BigDecimal totalDiscounts;

    @Schema(description = "Revenue growth percentage versus prior period", example = "18.5")
    private Double revenueGrowthPercentage;

    @Schema(description = "Timeline curve points for chart plotting")
    private List<TimeSeriesPointDto> revenueTrend;
}
