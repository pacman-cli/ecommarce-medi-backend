package com.example.ecommerce.dashboard.service;

import com.example.ecommerce.dashboard.dto.request.DashboardFilterRequest;
import com.example.ecommerce.dashboard.dto.request.ExportReportRequest;
import com.example.ecommerce.dashboard.dto.response.ChartDataResponse;
import com.example.ecommerce.dashboard.dto.response.CustomerAnalyticsResponse;
import com.example.ecommerce.dashboard.dto.response.DailySalesResponse;
import com.example.ecommerce.dashboard.dto.response.DashboardSummaryResponse;
import com.example.ecommerce.dashboard.dto.response.InventorySummaryResponse;
import com.example.ecommerce.dashboard.dto.response.LowStockProductResponse;
import com.example.ecommerce.dashboard.dto.response.MonthlySalesResponse;
import com.example.ecommerce.dashboard.dto.response.OrderStatisticsResponse;
import com.example.ecommerce.dashboard.dto.response.RecentOrderResponse;
import com.example.ecommerce.dashboard.dto.response.RevenueAnalyticsResponse;
import com.example.ecommerce.dashboard.dto.response.SalesAnalyticsResponse;
import com.example.ecommerce.dashboard.dto.response.TopCategoryResponse;
import com.example.ecommerce.dashboard.dto.response.TopCustomerResponse;
import com.example.ecommerce.dashboard.dto.response.TopSellingProductResponse;

import java.util.List;

/**
 * Service interface defining analytical aggregation metrics, chart data generation, and report export operations.
 */
public interface DashboardService {

    /**
     * Retrieves overall high-level executive dashboard summary.
     */
    DashboardSummaryResponse getDashboardSummary(DashboardFilterRequest filter);

    /**
     * Retrieves financial revenue performance and growth trends.
     */
    RevenueAnalyticsResponse getRevenueAnalytics(DashboardFilterRequest filter);

    /**
     * Retrieves sales volume, order counts, and unit sales metrics.
     */
    SalesAnalyticsResponse getSalesAnalytics(DashboardFilterRequest filter);

    /**
     * Retrieves order state statistics and status distributions.
     */
    OrderStatisticsResponse getOrderStatistics(DashboardFilterRequest filter);

    /**
     * Retrieves latest placed customer orders.
     */
    List<RecentOrderResponse> getRecentOrders(int limit);

    /**
     * Retrieves customer accounts analytics and active user metrics.
     */
    CustomerAnalyticsResponse getCustomerAnalytics(DashboardFilterRequest filter);

    /**
     * Retrieves highest spending customers.
     */
    List<TopCustomerResponse> getTopCustomers(DashboardFilterRequest filter);

    /**
     * Retrieves stock health, warehouse inventory counts, and total valuation.
     */
    InventorySummaryResponse getInventorySummary();

    /**
     * Retrieves products at or below low stock threshold.
     */
    List<LowStockProductResponse> getLowStockProducts(int limit);

    /**
     * Retrieves top selling products ordered by sales volume or revenue.
     */
    List<TopSellingProductResponse> getTopSellingProducts(DashboardFilterRequest filter);

    /**
     * Retrieves top performing product categories by revenue share.
     */
    List<TopCategoryResponse> getTopCategories(DashboardFilterRequest filter);

    /**
     * Retrieves monthly sales aggregation metrics.
     */
    List<MonthlySalesResponse> getMonthlySales(DashboardFilterRequest filter);

    /**
     * Retrieves daily sales breakdown metrics.
     */
    List<DailySalesResponse> getDailySales(DashboardFilterRequest filter);

    /**
     * Retrieves unified front-end chart visual dataset.
     */
    ChartDataResponse getChartData(DashboardFilterRequest filter);

    /**
     * Exports analytics report into downloadable byte array format (CSV/Excel).
     */
    byte[] exportReport(ExportReportRequest request);
}
