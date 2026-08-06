package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Segment item for pie, donut, or breakdown charts (order statuses, category revenue share).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Categorical slice representation for pie and breakdown charts")
public class PieChartSegmentDto {

    @Schema(description = "Category or segment title", example = "DELIVERED")
    private String label;

    @Schema(description = "Numerical metric value", example = "12500.00")
    private BigDecimal value;

    @Schema(description = "Percentage share of total aggregate", example = "45.5")
    private Double percentage;

    @Schema(description = "Suggested CSS hex color for charting library rendering", example = "#10B981")
    private String color;
}
