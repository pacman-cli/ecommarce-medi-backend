package com.example.ecommerce.scheduler.job;

import com.example.ecommerce.dashboard.dto.request.DashboardFilterRequest;
import com.example.ecommerce.dashboard.dto.response.DashboardSummaryResponse;
import com.example.ecommerce.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Background scheduled cron job generating weekly sales performance analytics.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeeklyAnalyticsScheduler {

    private final DashboardService dashboardService;

    @Scheduled(cron = "0 0 2 * * MON") // Mondays at 02:00 AM
    public void runWeeklyAnalytics() {
        log.info("[CRON JOB] Compiling weekly sales analytics...");
        LocalDate lastWeek = LocalDate.now().minusDays(7);
        LocalDate yesterday = LocalDate.now().minusDays(1);

        DashboardFilterRequest filter = DashboardFilterRequest.builder()
                .startDate(lastWeek)
                .endDate(yesterday)
                .build();

        DashboardSummaryResponse summary = dashboardService.getDashboardSummary(filter);

        log.info("[WEEKLY ANALYTICS SUMMARY] Period: {} to {} | Total Orders: {} | Total Sales: ${} | Avg Order Value: ${}",
                lastWeek, yesterday, summary.getTotalOrders(), summary.getTotalRevenue(), summary.getAverageOrderValue());
    }
}
