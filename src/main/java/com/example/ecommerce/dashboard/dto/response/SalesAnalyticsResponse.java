package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Detailed sales performance metrics and volume trends.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Sales performance analytics payload")
public class SalesAnalyticsResponse {

    @Schema(description = "Total placed sales orders in period", example = "420")
    private Long totalSalesCount;

    @Schema(description = "Total item units sold in period", example = "1280")
    private Long totalUnitsSold;

    @Schema(description = "Average Order Value (AOV)", example = "142.85")
    private BigDecimal averageOrderValue;

    @Schema(description = "Sales volume growth percentage versus prior period", example = "15.8")
    private Double salesGrowthPercentage;

    @Schema(description = "Daily sales trend points within period")
    private List<DailySalesResponse> dailySales;

    @Schema(description = "Monthly sales trend points within period")
    private List<MonthlySalesResponse> monthlySales;
}
