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
 * Background scheduled cron job generating monthly sales performance reports.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyReportScheduler {

    private final DashboardService dashboardService;

    @Scheduled(cron = "0 0 3 1 * ?") // 1st of every month at 03:00 AM
    public void runMonthlyReport() {
        log.info("[CRON JOB] Compiling monthly sales performance report...");
        LocalDate firstDayLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayLastMonth = LocalDate.now().withDayOfMonth(1).minusDays(1);

        DashboardFilterRequest filter = DashboardFilterRequest.builder()
                .startDate(firstDayLastMonth)
                .endDate(lastDayLastMonth)
                .build();

        DashboardSummaryResponse summary = dashboardService.getDashboardSummary(filter);

        log.info("[MONTHLY REPORT SUMMARY] Month: {} to {} | Total Revenue: ${} | Orders: {} | Net Growth Rate: {}%",
                firstDayLastMonth, lastDayLastMonth, summary.getTotalRevenue(), summary.getTotalOrders(), summary.getRevenueGrowthPercentage());
    }
}
