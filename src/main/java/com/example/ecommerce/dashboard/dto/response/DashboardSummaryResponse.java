package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Top-level overall executive dashboard summary envelope.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Overall top-level executive store dashboard summary KPIs")
public class DashboardSummaryResponse {

    @Schema(description = "Total revenue earned in period", example = "58420.00")
    private BigDecimal totalRevenue;

    @Schema(description = "Revenue growth percentage compared to prior period", example = "14.2")
    private Double revenueGrowthPercentage;

    @Schema(description = "Total orders placed in period", example = "420")
    private Long totalOrders;

    @Schema(description = "Order count growth percentage compared to prior period", example = "8.6")
    private Double orderGrowthPercentage;

    @Schema(description = "Total active customers", example = "1250")
    private Long totalCustomers;

    @Schema(description = "Customer growth percentage compared to prior period", example = "11.5")
    private Double customerGrowthPercentage;

    @Schema(description = "Average Order Value (AOV)", example = "139.10")
    private BigDecimal averageOrderValue;

    @Schema(description = "Count of products triggering low stock alert", example = "12")
    private Long lowStockAlertCount;

    @Schema(description = "Count of pending orders requiring fulfillment action", example = "8")
    private Long pendingOrdersCount;

    @Schema(description = "Recent orders snapshot list")
    private List<RecentOrderResponse> recentOrders;

    @Schema(description = "Top selling products snapshot list")
    private List<TopSellingProductResponse> topProducts;

    @Schema(description = "Top performing categories list")
    private List<TopCategoryResponse> topCategories;

    @Schema(description = "Stock warning items alert list")
    private List<LowStockProductResponse> lowStockProducts;

    @Schema(description = "Order status breakdown segment distribution")
    private List<PieChartSegmentDto> orderStatusDistribution;
}
