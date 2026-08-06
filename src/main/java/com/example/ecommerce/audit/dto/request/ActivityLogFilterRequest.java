package com.example.ecommerce.audit.dto.request;

import com.example.ecommerce.audit.dto.enums.ActivityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

/**
 * Filter criteria payload for querying user and admin activity logs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Activity log query filter criteria payload")
public class ActivityLogFilterRequest {

    @Schema(description = "Activity type filter", example = "ADMIN_ACTION")
    private ActivityType activityType;

    @Schema(description = "Module tag filter", example = "INVENTORY")
    private String module;

    @Schema(description = "User ID filter", example = "1")
    private Long userId;

    @Schema(description = "Filter only admin security activities", example = "true")
    private Boolean isAdminActivity;

    @Schema(description = "Keyword query matching description", example = "Warehouse")
    private String query;

    @Schema(description = "Start timestamp bound", example = "2026-08-01T00:00:00Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant startDate;

    @Schema(description = "End timestamp bound", example = "2026-08-31T23:59:59Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant endDate;
}
