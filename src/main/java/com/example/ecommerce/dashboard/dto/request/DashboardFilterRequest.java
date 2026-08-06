package com.example.ecommerce.dashboard.dto.request;

import com.example.ecommerce.dashboard.enums.AnalyticsPeriod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Filter parameter payload for requesting dashboard metrics across dynamic time ranges.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Filter request for dashboard analytics queries")
public class DashboardFilterRequest {

    @Schema(description = "Predefined analytics period", example = "THIS_MONTH")
    @Builder.Default
    private AnalyticsPeriod period = AnalyticsPeriod.THIS_MONTH;

    @Schema(description = "Custom start date (ISO format yyyy-MM-dd). Required if period is CUSTOM", example = "2026-08-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "Custom end date (ISO format yyyy-MM-dd). Required if period is CUSTOM", example = "2026-08-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Schema(description = "Maximum records to return for top lists (products, categories, customers)", example = "10")
    @Min(value = 1, message = "Limit must be at least 1")
    @Max(value = 100, message = "Limit cannot exceed 100")
    @Builder.Default
    private Integer limit = 10;
}
