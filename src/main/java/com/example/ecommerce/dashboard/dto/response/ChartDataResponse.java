package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Unified charts dataset payload specifically tailored for front-end charts (Chart.js / ApexCharts / Recharts).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Structured charting data payload for front-end visual widgets")
public class ChartDataResponse {

    @Schema(description = "Time-series points for revenue trend line/bar chart")
    private List<TimeSeriesPointDto> revenueChart;

    @Schema(description = "Time-series points for order volume trend line/bar chart")
    private List<TimeSeriesPointDto> orderVolumeChart;

    @Schema(description = "Pie/Donut chart slice distribution for order statuses")
    private List<PieChartSegmentDto> orderStatusPieChart;

    @Schema(description = "Pie/Donut chart slice distribution for top category revenue shares")
    private List<PieChartSegmentDto> categorySharePieChart;

    @Schema(description = "Bar chart distribution of stock inventory levels by category")
    private List<PieChartSegmentDto> inventoryHealthChart;
}
