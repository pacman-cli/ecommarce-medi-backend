package com.example.ecommerce.dashboard.service;

import com.example.ecommerce.dashboard.dto.request.DashboardFilterRequest;
import com.example.ecommerce.dashboard.dto.request.ExportReportRequest;
import com.example.ecommerce.dashboard.dto.response.ChartDataResponse;
import com.example.ecommerce.dashboard.dto.response.CustomerAnalyticsResponse;
import com.example.ecommerce.dashboard.dto.response.DashboardSummaryResponse;
import com.example.ecommerce.dashboard.dto.response.InventorySummaryResponse;
import com.example.ecommerce.dashboard.dto.response.LowStockProductResponse;
import com.example.ecommerce.dashboard.dto.response.OrderStatisticsResponse;
import com.example.ecommerce.dashboard.dto.response.RevenueAnalyticsResponse;
import com.example.ecommerce.dashboard.dto.response.SalesAnalyticsResponse;
import com.example.ecommerce.dashboard.enums.AnalyticsPeriod;
import com.example.ecommerce.dashboard.enums.ReportFormat;
import com.example.ecommerce.dashboard.mapper.DashboardMapper;
import com.example.ecommerce.dashboard.repository.DashboardRepository;
import com.example.ecommerce.dashboard.service.impl.DashboardServiceImpl;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.StockStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private DashboardMapper dashboardMapper;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private DashboardFilterRequest filterRequest;

    @BeforeEach
    void setUp() {
        filterRequest = DashboardFilterRequest.builder()
                .period(AnalyticsPeriod.THIS_MONTH)
                .limit(10)
                .build();
    }

    @Test
    void testGetDashboardSummary() {
        when(dashboardRepository.sumGrossRevenueBetween(any(), any())).thenReturn(new BigDecimal("1000.00"));
        when(dashboardRepository.countOrdersBetween(any(), any())).thenReturn(10L);
        when(dashboardRepository.countTotalCustomers()).thenReturn(50L);
        when(dashboardRepository.countNewCustomersBetween(any(), any())).thenReturn(5L);
        when(dashboardRepository.countLowStockProducts()).thenReturn(2L);
        when(dashboardRepository.countOrdersByStatusBetween(eq(OrderStatus.PENDING), any(), any())).thenReturn(1L);

        DashboardSummaryResponse summary = dashboardService.getDashboardSummary(filterRequest);

        assertNotNull(summary);
        assertEquals(new BigDecimal("1000.00"), summary.getTotalRevenue());
        assertEquals(10L, summary.getTotalOrders());
        assertEquals(50L, summary.getTotalCustomers());
        assertEquals(new BigDecimal("100.00"), summary.getAverageOrderValue());
    }

    @Test
    void testGetRevenueAnalytics() {
        when(dashboardRepository.sumGrossRevenueBetween(any(), any())).thenReturn(new BigDecimal("5000.00"));
        when(dashboardRepository.sumNetRevenueBetween(any(), any())).thenReturn(new BigDecimal("4200.00"));
        when(dashboardRepository.sumTaxBetween(any(), any())).thenReturn(new BigDecimal("500.00"));
        when(dashboardRepository.sumShippingBetween(any(), any())).thenReturn(new BigDecimal("300.00"));
        when(dashboardRepository.sumDiscountsBetween(any(), any())).thenReturn(new BigDecimal("100.00"));

        RevenueAnalyticsResponse response = dashboardService.getRevenueAnalytics(filterRequest);

        assertNotNull(response);
        assertEquals(new BigDecimal("5000.00"), response.getGrossRevenue());
        assertEquals(new BigDecimal("4200.00"), response.getNetRevenue());
    }

    @Test
    void testGetSalesAnalytics() {
        when(dashboardRepository.countOrdersBetween(any(), any())).thenReturn(20L);
        when(dashboardRepository.sumUnitsSoldBetween(any(), any())).thenReturn(60L);
        when(dashboardRepository.sumGrossRevenueBetween(any(), any())).thenReturn(new BigDecimal("3000.00"));

        SalesAnalyticsResponse response = dashboardService.getSalesAnalytics(filterRequest);

        assertNotNull(response);
        assertEquals(20L, response.getTotalSalesCount());
        assertEquals(60L, response.getTotalUnitsSold());
        assertEquals(new BigDecimal("150.00"), response.getAverageOrderValue());
    }

    @Test
    void testGetInventorySummary() {
        when(dashboardRepository.countTotalProducts()).thenReturn(100L);
        when(dashboardRepository.countInStockProducts()).thenReturn(85L);
        when(dashboardRepository.countLowStockProducts()).thenReturn(10L);
        when(dashboardRepository.countOutOfStockProducts()).thenReturn(5L);
        when(dashboardRepository.sumTotalQuantityInStock()).thenReturn(5000L);
        when(dashboardRepository.sumTotalInventoryValue()).thenReturn(new BigDecimal("75000.00"));

        InventorySummaryResponse summary = dashboardService.getInventorySummary();

        assertNotNull(summary);
        assertEquals(100L, summary.getTotalProducts());
        assertEquals(85L, summary.getInStockProducts());
        assertEquals(new BigDecimal("75000.00"), summary.getTotalInventoryValue());
    }

    @Test
    void testExportReport() {
        ExportReportRequest exportReq = ExportReportRequest.builder()
                .reportType("SALES")
                .format(ReportFormat.CSV)
                .period(AnalyticsPeriod.THIS_MONTH)
                .build();

        when(dashboardRepository.findOrdersBetweenForTrends(any(), any())).thenReturn(Collections.emptyList());

        byte[] reportBytes = dashboardService.exportReport(exportReq);

        assertNotNull(reportBytes);
        assertTrue(reportBytes.length > 0);
        String csvContent = new String(reportBytes);
        assertTrue(csvContent.contains("Date,Order Number"));
    }
}
