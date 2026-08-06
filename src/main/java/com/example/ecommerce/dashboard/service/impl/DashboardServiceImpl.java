package com.example.ecommerce.dashboard.service.impl;

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
import com.example.ecommerce.dashboard.dto.response.PieChartSegmentDto;
import com.example.ecommerce.dashboard.dto.response.RecentOrderResponse;
import com.example.ecommerce.dashboard.dto.response.RevenueAnalyticsResponse;
import com.example.ecommerce.dashboard.dto.response.SalesAnalyticsResponse;
import com.example.ecommerce.dashboard.dto.response.TimeSeriesPointDto;
import com.example.ecommerce.dashboard.dto.response.TopCategoryResponse;
import com.example.ecommerce.dashboard.dto.response.TopCustomerResponse;
import com.example.ecommerce.dashboard.dto.response.TopSellingProductResponse;
import com.example.ecommerce.dashboard.enums.AnalyticsPeriod;
import com.example.ecommerce.dashboard.mapper.DashboardMapper;
import com.example.ecommerce.dashboard.repository.DashboardRepository;
import com.example.ecommerce.dashboard.service.DashboardService;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderItem;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import com.example.ecommerce.product.entity.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Production-ready implementation of {@link DashboardService} delivering analytics,
 * growth calculations, chart data visual transformations, and report exports.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final DashboardMapper dashboardMapper;

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy");

    @Override
    public DashboardSummaryResponse getDashboardSummary(DashboardFilterRequest filter) {
        log.debug("Generating dashboard summary for period: {}", filter != null ? filter.getPeriod() : "default");
        TimeRange range = resolveTimeRange(filter);

        BigDecimal currentRevenue = dashboardRepository.sumGrossRevenueBetween(range.start, range.end);
        BigDecimal prevRevenue = dashboardRepository.sumGrossRevenueBetween(range.prevStart, range.prevEnd);
        Double revenueGrowth = calculateGrowth(currentRevenue, prevRevenue);

        Long currentOrders = dashboardRepository.countOrdersBetween(range.start, range.end);
        Long prevOrders = dashboardRepository.countOrdersBetween(range.prevStart, range.prevEnd);
        Double orderGrowth = calculateGrowth(currentOrders, prevOrders);

        Long totalCustomers = dashboardRepository.countTotalCustomers();
        Long newCustomersCurrent = dashboardRepository.countNewCustomersBetween(range.start, range.end);
        Long newCustomersPrev = dashboardRepository.countNewCustomersBetween(range.prevStart, range.prevEnd);
        Double customerGrowth = calculateGrowth(newCustomersCurrent, newCustomersPrev);

        BigDecimal averageOrderValue = BigDecimal.ZERO;
        if (currentOrders > 0) {
            averageOrderValue = currentRevenue.divide(BigDecimal.valueOf(currentOrders), 2, RoundingMode.HALF_UP);
        }

        Long lowStockAlertCount = dashboardRepository.countLowStockProducts();
        Long pendingOrdersCount = dashboardRepository.countOrdersByStatusBetween(OrderStatus.PENDING, range.start, range.end);

        int limit = (filter != null && filter.getLimit() != null) ? filter.getLimit() : 10;
        List<RecentOrderResponse> recentOrders = getRecentOrders(5);
        List<TopSellingProductResponse> topProducts = getTopSellingProducts(filter);
        List<TopCategoryResponse> topCategories = getTopCategories(filter);
        List<LowStockProductResponse> lowStockProducts = getLowStockProducts(limit);

        OrderStatisticsResponse stats = getOrderStatistics(filter);
        List<PieChartSegmentDto> statusDistribution = stats != null ? stats.getStatusBreakdown() : Collections.emptyList();

        return DashboardSummaryResponse.builder()
                .totalRevenue(currentRevenue)
                .revenueGrowthPercentage(revenueGrowth)
                .totalOrders(currentOrders)
                .orderGrowthPercentage(orderGrowth)
                .totalCustomers(totalCustomers)
                .customerGrowthPercentage(customerGrowth)
                .averageOrderValue(averageOrderValue)
                .lowStockAlertCount(lowStockAlertCount)
                .pendingOrdersCount(pendingOrdersCount)
                .recentOrders(recentOrders)
                .topProducts(topProducts)
                .topCategories(topCategories)
                .lowStockProducts(lowStockProducts)
                .orderStatusDistribution(statusDistribution)
                .build();
    }

    @Override
    public RevenueAnalyticsResponse getRevenueAnalytics(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);

        BigDecimal grossRevenue = dashboardRepository.sumGrossRevenueBetween(range.start, range.end);
        BigDecimal prevGrossRevenue = dashboardRepository.sumGrossRevenueBetween(range.prevStart, range.prevEnd);
        BigDecimal netRevenue = dashboardRepository.sumNetRevenueBetween(range.start, range.end);
        BigDecimal totalTax = dashboardRepository.sumTaxBetween(range.start, range.end);
        BigDecimal totalShipping = dashboardRepository.sumShippingBetween(range.start, range.end);
        BigDecimal totalDiscounts = dashboardRepository.sumDiscountsBetween(range.start, range.end);

        Double revenueGrowth = calculateGrowth(grossRevenue, prevGrossRevenue);
        List<DailySalesResponse> dailySales = getDailySales(filter);

        List<TimeSeriesPointDto> trend = dailySales.stream()
                .map(d -> TimeSeriesPointDto.builder()
                        .label(d.getDateLabel())
                        .amount(d.getTotalRevenue())
                        .count(d.getOrderCount())
                        .build())
                .collect(Collectors.toList());

        return RevenueAnalyticsResponse.builder()
                .grossRevenue(grossRevenue)
                .netRevenue(netRevenue)
                .totalTax(totalTax)
                .totalShipping(totalShipping)
                .totalDiscounts(totalDiscounts)
                .revenueGrowthPercentage(revenueGrowth)
                .revenueTrend(trend)
                .build();
    }

    @Override
    public SalesAnalyticsResponse getSalesAnalytics(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);

        Long currentSalesCount = dashboardRepository.countOrdersBetween(range.start, range.end);
        Long prevSalesCount = dashboardRepository.countOrdersBetween(range.prevStart, range.prevEnd);
        Long totalUnitsSold = dashboardRepository.sumUnitsSoldBetween(range.start, range.end);
        BigDecimal grossRevenue = dashboardRepository.sumGrossRevenueBetween(range.start, range.end);

        Double salesGrowth = calculateGrowth(currentSalesCount, prevSalesCount);
        BigDecimal aov = BigDecimal.ZERO;
        if (currentSalesCount > 0) {
            aov = grossRevenue.divide(BigDecimal.valueOf(currentSalesCount), 2, RoundingMode.HALF_UP);
        }

        List<DailySalesResponse> dailySales = getDailySales(filter);
        List<MonthlySalesResponse> monthlySales = getMonthlySales(filter);

        return SalesAnalyticsResponse.builder()
                .totalSalesCount(currentSalesCount)
                .totalUnitsSold(totalUnitsSold)
                .averageOrderValue(aov)
                .salesGrowthPercentage(salesGrowth)
                .dailySales(dailySales)
                .monthlySales(monthlySales)
                .build();
    }

    @Override
    public OrderStatisticsResponse getOrderStatistics(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);

        Long total = dashboardRepository.countOrdersBetween(range.start, range.end);
        Long pending = dashboardRepository.countOrdersByStatusBetween(OrderStatus.PENDING, range.start, range.end);
        Long confirmed = dashboardRepository.countOrdersByStatusBetween(OrderStatus.CONFIRMED, range.start, range.end);
        Long packed = dashboardRepository.countOrdersByStatusBetween(OrderStatus.PACKED, range.start, range.end);
        Long shipped = dashboardRepository.countOrdersByStatusBetween(OrderStatus.SHIPPED, range.start, range.end);
        Long delivered = dashboardRepository.countOrdersByStatusBetween(OrderStatus.DELIVERED, range.start, range.end);
        Long cancelled = dashboardRepository.countOrdersByStatusBetween(OrderStatus.CANCELLED, range.start, range.end);
        Long returned = dashboardRepository.countOrdersByStatusBetween(OrderStatus.RETURNED, range.start, range.end);
        Long refunded = dashboardRepository.countOrdersByStatusBetween(OrderStatus.REFUNDED, range.start, range.end);

        List<PieChartSegmentDto> statusSegments = new ArrayList<>();
        statusSegments.add(buildSegment("Pending", pending, total, "#F59E0B"));
        statusSegments.add(buildSegment("Confirmed", confirmed, total, "#3B82F6"));
        statusSegments.add(buildSegment("Packed", packed, total, "#8B5CF6"));
        statusSegments.add(buildSegment("Shipped", shipped, total, "#06B6D4"));
        statusSegments.add(buildSegment("Delivered", delivered, total, "#10B981"));
        statusSegments.add(buildSegment("Cancelled", cancelled, total, "#EF4444"));
        statusSegments.add(buildSegment("Returned", returned, total, "#F97316"));
        statusSegments.add(buildSegment("Refunded", refunded, total, "#6B7280"));

        List<PieChartSegmentDto> paymentSegments = new ArrayList<>();
        for (PaymentStatus ps : PaymentStatus.values()) {
            Long count = dashboardRepository.countOrdersByPaymentStatusBetween(ps, range.start, range.end);
            String color = switch (ps) {
                case PAID -> "#10B981";
                case PENDING -> "#F59E0B";
                case FAILED -> "#EF4444";
                case REFUNDED -> "#6B7280";
                default -> "#9CA3AF";
            };
            paymentSegments.add(buildSegment(ps.name(), count, total, color));
        }

        return OrderStatisticsResponse.builder()
                .totalOrders(total)
                .pendingOrders(pending)
                .confirmedOrders(confirmed)
                .packedOrders(packed)
                .shippedOrders(shipped)
                .deliveredOrders(delivered)
                .cancelledOrders(cancelled)
                .returnedOrders(returned)
                .refundedOrders(refunded)
                .statusBreakdown(statusSegments)
                .paymentStatusBreakdown(paymentSegments)
                .build();
    }

    @Override
    public List<RecentOrderResponse> getRecentOrders(int limit) {
        int targetLimit = limit > 0 ? Math.min(limit, 50) : 10;
        Pageable pageable = PageRequest.of(0, targetLimit);
        List<Order> orders = dashboardRepository.findRecentOrders(pageable);
        return dashboardMapper.toRecentOrderResponseList(orders);
    }

    @Override
    public CustomerAnalyticsResponse getCustomerAnalytics(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);

        Long totalCustomers = dashboardRepository.countTotalCustomers();
        Long newCustomers = dashboardRepository.countNewCustomersBetween(range.start, range.end);
        Long activeCustomers = dashboardRepository.countActiveCustomersBetween(range.start, range.end);

        Long prevNewCustomers = dashboardRepository.countNewCustomersBetween(range.prevStart, range.prevEnd);
        Double growth = calculateGrowth(newCustomers, prevNewCustomers);

        Double repeatRate = 0.0;
        if (totalCustomers > 0) {
            repeatRate = BigDecimal.valueOf(activeCustomers)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalCustomers), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        List<TopCustomerResponse> topCustomers = getTopCustomers(filter);

        return CustomerAnalyticsResponse.builder()
                .totalCustomers(totalCustomers)
                .newCustomers(newCustomers)
                .activeCustomers(activeCustomers)
                .repeatPurchaseRate(repeatRate)
                .growthPercentage(growth)
                .topCustomers(topCustomers)
                .build();
    }

    @Override
    public List<TopCustomerResponse> getTopCustomers(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);
        int limit = (filter != null && filter.getLimit() != null) ? filter.getLimit() : 10;
        Pageable pageable = PageRequest.of(0, limit);

        List<Object[]> rows = dashboardRepository.findTopCustomersBetween(range.start, range.end, pageable);
        return rows.stream()
                .map(dashboardMapper::toTopCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InventorySummaryResponse getInventorySummary() {
        Long totalProducts = dashboardRepository.countTotalProducts();
        Long inStock = dashboardRepository.countInStockProducts();
        Long lowStock = dashboardRepository.countLowStockProducts();
        Long outOfStock = dashboardRepository.countOutOfStockProducts();
        Long totalQuantity = dashboardRepository.sumTotalQuantityInStock();
        BigDecimal totalValuation = dashboardRepository.sumTotalInventoryValue();

        return InventorySummaryResponse.builder()
                .totalProducts(totalProducts)
                .inStockProducts(inStock)
                .lowStockProducts(lowStock)
                .outOfStockProducts(outOfStock)
                .totalQuantityInStock(totalQuantity)
                .totalInventoryValue(totalValuation)
                .build();
    }

    @Override
    public List<LowStockProductResponse> getLowStockProducts(int limit) {
        int targetLimit = limit > 0 ? Math.min(limit, 100) : 10;
        Pageable pageable = PageRequest.of(0, targetLimit);
        List<Product> products = dashboardRepository.findLowStockProducts(pageable);
        return dashboardMapper.toLowStockProductResponseList(products);
    }

    @Override
    public List<TopSellingProductResponse> getTopSellingProducts(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);
        int limit = (filter != null && filter.getLimit() != null) ? filter.getLimit() : 10;
        Pageable pageable = PageRequest.of(0, limit);

        List<Object[]> rows = dashboardRepository.findTopSellingProductsBetween(range.start, range.end, pageable);
        return rows.stream()
                .map(dashboardMapper::toTopSellingProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopCategoryResponse> getTopCategories(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);
        int limit = (filter != null && filter.getLimit() != null) ? filter.getLimit() : 10;
        Pageable pageable = PageRequest.of(0, limit);

        BigDecimal totalStoreRevenue = dashboardRepository.sumGrossRevenueBetween(range.start, range.end);
        List<Object[]> rows = dashboardRepository.findTopCategoriesBetween(range.start, range.end, pageable);

        return rows.stream()
                .map(r -> dashboardMapper.toTopCategoryResponse(r, totalStoreRevenue))
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlySalesResponse> getMonthlySales(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);
        List<Order> orders = dashboardRepository.findOrdersBetweenForTrends(range.start, range.end);

        Map<YearMonth, MonthlySalesResponse> monthlyMap = new TreeMap<>();
        YearMonth startMonth = YearMonth.from(range.startLocalDate);
        YearMonth endMonth = YearMonth.from(range.endLocalDate);

        YearMonth currentMonth = startMonth;
        while (!currentMonth.isAfter(endMonth)) {
            monthlyMap.put(currentMonth, MonthlySalesResponse.builder()
                    .year(currentMonth.getYear())
                    .month(currentMonth.getMonthValue())
                    .monthName(currentMonth.format(MONTH_FORMATTER))
                    .totalRevenue(BigDecimal.ZERO)
                    .orderCount(0L)
                    .totalItemsSold(0L)
                    .averageOrderValue(BigDecimal.ZERO)
                    .build());
            currentMonth = currentMonth.plusMonths(1);
        }

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                continue;
            }
            LocalDate orderDate = LocalDate.ofInstant(order.getCreatedAt(), DEFAULT_ZONE);
            YearMonth ym = YearMonth.from(orderDate);
            MonthlySalesResponse current = monthlyMap.get(ym);
            if (current != null) {
                BigDecimal newRev = current.getTotalRevenue().add(order.getGrandTotal() != null ? order.getGrandTotal() : BigDecimal.ZERO);
                long newCount = current.getOrderCount() + 1;
                long itemsCount = current.getTotalItemsSold() + (order.getItems() != null ? order.getItems().stream().mapToInt(OrderItem::getQuantity).sum() : 0);

                current.setTotalRevenue(newRev);
                current.setOrderCount(newCount);
                current.setTotalItemsSold(itemsCount);
            }
        }

        for (MonthlySalesResponse resp : monthlyMap.values()) {
            if (resp.getOrderCount() > 0) {
                resp.setAverageOrderValue(resp.getTotalRevenue().divide(BigDecimal.valueOf(resp.getOrderCount()), 2, RoundingMode.HALF_UP));
            }
        }

        return new ArrayList<>(monthlyMap.values());
    }

    @Override
    public List<DailySalesResponse> getDailySales(DashboardFilterRequest filter) {
        TimeRange range = resolveTimeRange(filter);
        List<Order> orders = dashboardRepository.findOrdersBetweenForTrends(range.start, range.end);

        Map<LocalDate, DailySalesResponse> dailyMap = new TreeMap<>();
        LocalDate currentDate = range.startLocalDate;
        while (!currentDate.isAfter(range.endLocalDate)) {
            dailyMap.put(currentDate, DailySalesResponse.builder()
                    .date(currentDate)
                    .dateLabel(currentDate.format(DATE_FORMATTER))
                    .totalRevenue(BigDecimal.ZERO)
                    .orderCount(0L)
                    .totalItemsSold(0L)
                    .averageOrderValue(BigDecimal.ZERO)
                    .build());
            currentDate = currentDate.plusDays(1);
        }

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                continue;
            }
            LocalDate orderDate = LocalDate.ofInstant(order.getCreatedAt(), DEFAULT_ZONE);
            DailySalesResponse current = dailyMap.get(orderDate);
            if (current != null) {
                BigDecimal newRev = current.getTotalRevenue().add(order.getGrandTotal() != null ? order.getGrandTotal() : BigDecimal.ZERO);
                long newCount = current.getOrderCount() + 1;
                long itemsCount = current.getTotalItemsSold() + (order.getItems() != null ? order.getItems().stream().mapToInt(OrderItem::getQuantity).sum() : 0);

                current.setTotalRevenue(newRev);
                current.setOrderCount(newCount);
                current.setTotalItemsSold(itemsCount);
            }
        }

        for (DailySalesResponse resp : dailyMap.values()) {
            if (resp.getOrderCount() > 0) {
                resp.setAverageOrderValue(resp.getTotalRevenue().divide(BigDecimal.valueOf(resp.getOrderCount()), 2, RoundingMode.HALF_UP));
            }
        }

        return new ArrayList<>(dailyMap.values());
    }

    @Override
    public ChartDataResponse getChartData(DashboardFilterRequest filter) {
        List<DailySalesResponse> dailySales = getDailySales(filter);
        OrderStatisticsResponse stats = getOrderStatistics(filter);
        List<TopCategoryResponse> topCategories = getTopCategories(filter);

        List<TimeSeriesPointDto> revenuePoints = dailySales.stream()
                .map(d -> TimeSeriesPointDto.builder()
                        .label(d.getDateLabel())
                        .amount(d.getTotalRevenue())
                        .count(d.getOrderCount())
                        .build())
                .collect(Collectors.toList());

        List<TimeSeriesPointDto> orderPoints = dailySales.stream()
                .map(d -> TimeSeriesPointDto.builder()
                        .label(d.getDateLabel())
                        .amount(BigDecimal.valueOf(d.getOrderCount()))
                        .count(d.getOrderCount())
                        .build())
                .collect(Collectors.toList());

        List<PieChartSegmentDto> categorySegments = topCategories.stream()
                .map(c -> PieChartSegmentDto.builder()
                        .label(c.getCategoryName())
                        .value(c.getTotalRevenue())
                        .percentage(c.getSharePercentage())
                        .color("#" + Integer.toHexString(c.getCategoryName().hashCode()).substring(0, 6))
                        .build())
                .collect(Collectors.toList());

        InventorySummaryResponse inventory = getInventorySummary();
        List<PieChartSegmentDto> inventoryHealth = Arrays.asList(
                PieChartSegmentDto.builder().label("In Stock").value(BigDecimal.valueOf(inventory.getInStockProducts())).color("#10B981").build(),
                PieChartSegmentDto.builder().label("Low Stock").value(BigDecimal.valueOf(inventory.getLowStockProducts())).color("#F59E0B").build(),
                PieChartSegmentDto.builder().label("Out of Stock").value(BigDecimal.valueOf(inventory.getOutOfStockProducts())).color("#EF4444").build()
        );

        return ChartDataResponse.builder()
                .revenueChart(revenuePoints)
                .orderVolumeChart(orderPoints)
                .orderStatusPieChart(stats != null ? stats.getStatusBreakdown() : Collections.emptyList())
                .categorySharePieChart(categorySegments)
                .inventoryHealthChart(inventoryHealth)
                .build();
    }

    @Override
    public byte[] exportReport(ExportReportRequest request) {
        if (request == null || request.getReportType() == null) {
            throw new BadRequestException("Report type must be specified");
        }

        DashboardFilterRequest filter = DashboardFilterRequest.builder()
                .period(request.getPeriod())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .limit(1000)
                .build();

        StringBuilder csv = new StringBuilder();

        switch (request.getReportType().toUpperCase()) {
            case "SALES" -> {
                csv.append("Date,Order Number,Customer Name,Customer Email,Status,Payment Status,Subtotal,Tax,Shipping,Grand Total\n");
                TimeRange range = resolveTimeRange(filter);
                List<Order> orders = dashboardRepository.findOrdersBetweenForTrends(range.start, range.end);
                for (Order o : orders) {
                    String customerName = o.getUser() != null ? (o.getUser().getFirstName() + " " + o.getUser().getLastName()) : "Guest";
                    String customerEmail = o.getUser() != null ? o.getUser().getEmail() : "guest@example.com";
                    csv.append(String.format("%s,\"%s\",\"%s\",\"%s\",%s,%s,%.2f,%.2f,%.2f,%.2f\n",
                            LocalDate.ofInstant(o.getCreatedAt(), DEFAULT_ZONE),
                            o.getOrderNumber(),
                            customerName,
                            customerEmail,
                            o.getStatus(),
                            o.getPaymentStatus(),
                            o.getSubtotal(),
                            o.getTaxAmount(),
                            o.getShippingFee(),
                            o.getGrandTotal()));
                }
            }
            case "INVENTORY" -> {
                csv.append("Product ID,SKU,Product Name,Category,Price,Quantity,Low Stock Threshold,Stock Status\n");
                List<LowStockProductResponse> products = getLowStockProducts(1000);
                for (LowStockProductResponse p : products) {
                    csv.append(String.format("%d,\"%s\",\"%s\",\"%s\",%.2f,%d,%d,%s\n",
                            p.getId(), p.getSku(), p.getName(), p.getCategoryName(),
                            p.getPrice(), p.getCurrentStock(), p.getMinStockThreshold(), p.getStockStatus()));
                }
            }
            case "CUSTOMERS" -> {
                csv.append("User ID,Full Name,Email,Phone,Total Orders,Total Spent,Last Order Date\n");
                List<TopCustomerResponse> customers = getTopCustomers(filter);
                for (TopCustomerResponse c : customers) {
                    csv.append(String.format("%d,\"%s\",\"%s\",\"%s\",%d,%.2f,%s\n",
                            c.getUserId(), c.getFullName(), c.getEmail(), c.getPhone(),
                            c.getTotalOrders(), c.getTotalSpent(), c.getLastOrderDate()));
                }
            }
            case "PRODUCTS" -> {
                csv.append("Product ID,SKU,Product Name,Category,Quantity Sold,Total Revenue\n");
                List<TopSellingProductResponse> topProds = getTopSellingProducts(filter);
                for (TopSellingProductResponse p : topProds) {
                    csv.append(String.format("%d,\"%s\",\"%s\",\"%s\",%d,%.2f\n",
                            p.getProductId(), p.getSku(), p.getProductName(), p.getCategoryName(),
                            p.getTotalQuantitySold(), p.getTotalRevenue()));
                }
            }
            default -> throw new BadRequestException("Unsupported report type: " + request.getReportType());
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    // --- Helper Methods ---

    private PieChartSegmentDto buildSegment(String label, Long count, Long total, String color) {
        double percentage = 0.0;
        if (total != null && total > 0 && count != null) {
            percentage = BigDecimal.valueOf(count)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        return PieChartSegmentDto.builder()
                .label(label)
                .value(BigDecimal.valueOf(count != null ? count : 0))
                .percentage(percentage)
                .color(color)
                .build();
    }

    private Double calculateGrowth(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return (current != null && current.compareTo(BigDecimal.ZERO) > 0) ? 100.0 : 0.0;
        }
        if (current == null) {
            return -100.0;
        }
        return current.subtract(previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(previous, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private Double calculateGrowth(Long current, Long previous) {
        if (previous == null || previous == 0) {
            return (current != null && current > 0) ? 100.0 : 0.0;
        }
        if (current == null) {
            return -100.0;
        }
        return BigDecimal.valueOf(current - previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(previous), 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private TimeRange resolveTimeRange(DashboardFilterRequest filter) {
        AnalyticsPeriod period = (filter != null && filter.getPeriod() != null) ? filter.getPeriod() : AnalyticsPeriod.THIS_MONTH;
        LocalDate today = LocalDate.now(DEFAULT_ZONE);

        LocalDate startDate;
        LocalDate endDate = today;

        switch (period) {
            case TODAY -> startDate = today;
            case YESTERDAY -> {
                startDate = today.minusDays(1);
                endDate = today.minusDays(1);
            }
            case THIS_WEEK -> startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case THIS_MONTH -> startDate = today.with(TemporalAdjusters.firstDayOfMonth());
            case THIS_QUARTER -> startDate = today.with(today.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth());
            case THIS_YEAR -> startDate = today.with(TemporalAdjusters.firstDayOfYear());
            case CUSTOM -> {
                if (filter == null || filter.getStartDate() == null || filter.getEndDate() == null) {
                    throw new BadRequestException("Start date and end date are required for CUSTOM analytics period");
                }
                if (filter.getStartDate().isAfter(filter.getEndDate())) {
                    throw new BadRequestException("Start date cannot be after end date");
                }
                startDate = filter.getStartDate();
                endDate = filter.getEndDate();
            }
            default -> startDate = today.with(TemporalAdjusters.firstDayOfMonth());
        }

        Instant startInstant = startDate.atStartOfDay(DEFAULT_ZONE).toInstant();
        Instant endInstant = endDate.atTime(LocalTime.MAX).atZone(DEFAULT_ZONE).toInstant();

        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        LocalDate prevStartDate = startDate.minusDays(days);
        LocalDate prevEndDate = endDate.minusDays(days);

        Instant prevStartInstant = prevStartDate.atStartOfDay(DEFAULT_ZONE).toInstant();
        Instant prevEndInstant = prevEndDate.atTime(LocalTime.MAX).atZone(DEFAULT_ZONE).toInstant();

        return new TimeRange(startInstant, endInstant, prevStartInstant, prevEndInstant, startDate, endDate);
    }

    private record TimeRange(
            Instant start,
            Instant end,
            Instant prevStart,
            Instant prevEnd,
            LocalDate startLocalDate,
            LocalDate endLocalDate
    ) {}
}
