package com.example.ecommerce.scheduler.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.scheduler.job.DailySalesReportScheduler;
import com.example.ecommerce.scheduler.job.ExpiredCouponScheduler;
import com.example.ecommerce.scheduler.job.ExpiredProductScheduler;
import com.example.ecommerce.scheduler.job.LowStockScheduler;
import com.example.ecommerce.scheduler.job.MonthlyReportScheduler;
import com.example.ecommerce.scheduler.job.SystemCleanupScheduler;
import com.example.ecommerce.scheduler.job.WeeklyAnalyticsScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for manually triggering background scheduled cron jobs on demand.
 */
@RestController
@RequestMapping("/api/v1/scheduler")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Scheduler & Cron Jobs", description = "Endpoints for inspecting and manually executing background scheduled cron jobs")
public class SchedulerController {

    private final LowStockScheduler lowStockScheduler;
    private final ExpiredCouponScheduler expiredCouponScheduler;
    private final ExpiredProductScheduler expiredProductScheduler;
    private final DailySalesReportScheduler dailySalesReportScheduler;
    private final WeeklyAnalyticsScheduler weeklyAnalyticsScheduler;
    private final MonthlyReportScheduler monthlyReportScheduler;
    private final SystemCleanupScheduler systemCleanupScheduler;

    @PostMapping("/run/low-stock")
    @Operation(summary = "Run low stock check job", description = "Manually triggers inventory low stock monitoring scan")
    public ResponseEntity<ApiResponse<Void>> runLowStockJob() {
        lowStockScheduler.runLowStockCheck();
        return ResponseEntity.ok(ApiResponse.success(null, "Low stock check cron job executed successfully"));
    }

    @PostMapping("/run/expired-coupons")
    @Operation(summary = "Run expired coupon check job", description = "Manually triggers expired promo coupon deactivation task")
    public ResponseEntity<ApiResponse<Void>> runExpiredCouponsJob() {
        expiredCouponScheduler.runExpiredCouponCheck();
        return ResponseEntity.ok(ApiResponse.success(null, "Expired coupon check cron job executed successfully"));
    }

    @PostMapping("/run/expired-products")
    @Operation(summary = "Run expired product stock batch job", description = "Manually triggers expired stock batch status update task")
    public ResponseEntity<ApiResponse<Void>> runExpiredProductsJob() {
        expiredProductScheduler.runExpiredProductCheck();
        return ResponseEntity.ok(ApiResponse.success(null, "Expired stock batch check cron job executed successfully"));
    }

    @PostMapping("/run/daily-sales")
    @Operation(summary = "Run daily sales report job", description = "Manually triggers daily sales performance report calculation")
    public ResponseEntity<ApiResponse<Void>> runDailySalesJob() {
        dailySalesReportScheduler.runDailySalesReport();
        return ResponseEntity.ok(ApiResponse.success(null, "Daily sales report cron job executed successfully"));
    }

    @PostMapping("/run/weekly-analytics")
    @Operation(summary = "Run weekly analytics job", description = "Manually triggers weekly performance summary calculation")
    public ResponseEntity<ApiResponse<Void>> runWeeklyAnalyticsJob() {
        weeklyAnalyticsScheduler.runWeeklyAnalytics();
        return ResponseEntity.ok(ApiResponse.success(null, "Weekly analytics cron job executed successfully"));
    }

    @PostMapping("/run/monthly-report")
    @Operation(summary = "Run monthly report job", description = "Manually triggers monthly performance report calculation")
    public ResponseEntity<ApiResponse<Void>> runMonthlyReportJob() {
        monthlyReportScheduler.runMonthlyReport();
        return ResponseEntity.ok(ApiResponse.success(null, "Monthly report cron job executed successfully"));
    }

    @PostMapping("/run/system-cleanup")
    @Operation(summary = "Run system cleanup job", description = "Manually triggers system maintenance and automated log purging task")
    public ResponseEntity<ApiResponse<Void>> runSystemCleanupJob() {
        systemCleanupScheduler.runSystemCleanup();
        return ResponseEntity.ok(ApiResponse.success(null, "System cleanup cron job executed successfully"));
    }
}
