package com.example.ecommerce.dashboard.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
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
import com.example.ecommerce.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing administrative analytics, store KPI summaries, charts APIs,
 * inventory health reports and downloadable data exports.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Dashboard & Analytics", description = "Endpoints for revenue analytics, sales trends, inventory health, order statistics, and report exports")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get overall dashboard summary", description = "Retrieves executive store overview KPIs including revenue, growth %, active orders, top categories, and low stock count")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dashboard summary retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Admin or Moderator role required")
    })
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummary(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        DashboardSummaryResponse response = dashboardService.getDashboardSummary(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Dashboard summary retrieved successfully"));
    }

    @GetMapping("/revenue")
    @Operation(summary = "Get revenue analytics", description = "Retrieves gross/net revenue breakdown, collected tax, shipping fees, discounts, and revenue trend points")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Revenue analytics retrieved successfully")
    })
    public ResponseEntity<ApiResponse<RevenueAnalyticsResponse>> getRevenueAnalytics(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        RevenueAnalyticsResponse response = dashboardService.getRevenueAnalytics(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Revenue analytics retrieved successfully"));
    }

    @GetMapping("/sales")
    @Operation(summary = "Get sales analytics", description = "Retrieves total order counts, units sold, Average Order Value (AOV), and growth rates")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sales analytics retrieved successfully")
    })
    public ResponseEntity<ApiResponse<SalesAnalyticsResponse>> getSalesAnalytics(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        SalesAnalyticsResponse response = dashboardService.getSalesAnalytics(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Sales analytics retrieved successfully"));
    }

    @GetMapping("/orders/statistics")
    @Operation(summary = "Get order statistics", description = "Retrieves order status and payment status counts and pie chart breakdown percentage distributions")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order statistics retrieved successfully")
    })
    public ResponseEntity<ApiResponse<OrderStatisticsResponse>> getOrderStatistics(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        OrderStatisticsResponse response = dashboardService.getOrderStatistics(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Order statistics retrieved successfully"));
    }

    @GetMapping("/orders/recent")
    @Operation(summary = "Get recent orders", description = "Retrieves latest customer order placements for dashboard listing table")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recent orders retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<RecentOrderResponse>>> getRecentOrders(
            @Parameter(description = "Maximum orders count", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        List<RecentOrderResponse> list = dashboardService.getRecentOrders(limit);
        return ResponseEntity.ok(ApiResponse.success(list, "Recent orders retrieved successfully"));
    }

    @GetMapping("/customers")
    @Operation(summary = "Get customer analytics", description = "Retrieves total customer count, new account signups, active users, and top spenders")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer analytics retrieved successfully")
    })
    public ResponseEntity<ApiResponse<CustomerAnalyticsResponse>> getCustomerAnalytics(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        CustomerAnalyticsResponse response = dashboardService.getCustomerAnalytics(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Customer analytics retrieved successfully"));
    }

    @GetMapping("/customers/top")
    @Operation(summary = "Get top spending customers", description = "Retrieves highest lifetime spending customer accounts")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Top customers retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<TopCustomerResponse>>> getTopCustomers(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        List<TopCustomerResponse> response = dashboardService.getTopCustomers(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Top customers retrieved successfully"));
    }

    @GetMapping("/inventory")
    @Operation(summary = "Get inventory summary", description = "Retrieves catalogue stock evaluation, low stock count, out-of-stock items, and total inventory monetary value")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inventory summary retrieved successfully")
    })
    public ResponseEntity<ApiResponse<InventorySummaryResponse>> getInventorySummary() {
        InventorySummaryResponse response = dashboardService.getInventorySummary();
        return ResponseEntity.ok(ApiResponse.success(response, "Inventory summary retrieved successfully"));
    }

    @GetMapping("/inventory/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieves list of products at or below configured low stock threshold")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<LowStockProductResponse>>> getLowStockProducts(
            @Parameter(description = "Maximum limit", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        List<LowStockProductResponse> response = dashboardService.getLowStockProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(response, "Low stock products retrieved successfully"));
    }

    @GetMapping("/top-products")
    @Operation(summary = "Get top selling products", description = "Retrieves best-selling products ranked by sales volume or gross revenue")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Top selling products retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<TopSellingProductResponse>>> getTopSellingProducts(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        List<TopSellingProductResponse> response = dashboardService.getTopSellingProducts(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Top selling products retrieved successfully"));
    }

    @GetMapping("/top-categories")
    @Operation(summary = "Get top categories", description = "Retrieves top performing product categories by total sales revenue share")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Top categories retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<TopCategoryResponse>>> getTopCategories(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        List<TopCategoryResponse> response = dashboardService.getTopCategories(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Top categories retrieved successfully"));
    }

    @GetMapping("/sales/monthly")
    @Operation(summary = "Get monthly sales breakdown", description = "Retrieves monthly aggregated revenue and order counts for comparison")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Monthly sales retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<MonthlySalesResponse>>> getMonthlySales(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        List<MonthlySalesResponse> response = dashboardService.getMonthlySales(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Monthly sales retrieved successfully"));
    }

    @GetMapping("/sales/daily")
    @Operation(summary = "Get daily sales breakdown", description = "Retrieves daily aggregated sales data over the selected date range")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Daily sales retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<DailySalesResponse>>> getDailySales(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        List<DailySalesResponse> response = dashboardService.getDailySales(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Daily sales retrieved successfully"));
    }

    @GetMapping("/charts")
    @Operation(summary = "Get unified charts dataset", description = "Retrieves pre-packaged dataset format for front-end charts (Chart.js / ApexCharts / Recharts)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Chart data retrieved successfully")
    })
    public ResponseEntity<ApiResponse<ChartDataResponse>> getChartData(
            @Valid @ModelAttribute DashboardFilterRequest filter) {
        ChartDataResponse response = dashboardService.getChartData(filter);
        return ResponseEntity.ok(ApiResponse.success(response, "Chart data retrieved successfully"));
    }

    @GetMapping("/export")
    @Operation(summary = "Export analytics report", description = "Generates and streams downloadable CSV report files (SALES, INVENTORY, CUSTOMERS, PRODUCTS)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Report file exported successfully")
    })
    public ResponseEntity<byte[]> exportReport(
            @Valid @ModelAttribute ExportReportRequest request) {
        byte[] fileBytes = dashboardService.exportReport(request);
        String fileName = String.format("%s_report_%s.csv", request.getReportType().toLowerCase(), System.currentTimeMillis());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(fileBytes);
    }
}
