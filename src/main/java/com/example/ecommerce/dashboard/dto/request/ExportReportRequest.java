package com.example.ecommerce.dashboard.dto.request;

import com.example.ecommerce.dashboard.enums.AnalyticsPeriod;
import com.example.ecommerce.dashboard.enums.ReportFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Filter payload for generating downloadable export reports.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request parameters for report export generation")
public class ExportReportRequest {

    @Schema(description = "Report subject area type", example = "SALES", allowableValues = {"SALES", "INVENTORY", "CUSTOMERS", "PRODUCTS"})
    @NotNull(message = "Report type must be specified")
    private String reportType;

    @Schema(description = "Export format", example = "CSV")
    @Builder.Default
    private ReportFormat format = ReportFormat.CSV;

    @Schema(description = "Analytics period", example = "THIS_MONTH")
    @Builder.Default
    private AnalyticsPeriod period = AnalyticsPeriod.THIS_MONTH;

    @Schema(description = "Custom start date", example = "2026-08-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "Custom end date", example = "2026-08-31")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
