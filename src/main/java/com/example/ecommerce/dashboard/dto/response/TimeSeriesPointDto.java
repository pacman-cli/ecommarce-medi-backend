package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data point element for time-series charts (daily/monthly revenue, orders, growth).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Single metric entry along a timeline axis")
public class TimeSeriesPointDto {

    @Schema(description = "Date or time period label (e.g. 2026-08-04 or Aug 2026)", example = "2026-08-04")
    private String label;

    @Schema(description = "Monetary or decimal value for period", example = "14250.50")
    private BigDecimal amount;

    @Schema(description = "Count or quantity metric for period", example = "42")
    private Long count;
}
