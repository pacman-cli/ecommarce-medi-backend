package com.example.ecommerce.audit.dto.response;

import com.example.ecommerce.audit.dto.enums.ActivityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * User and admin activity log response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Activity event log response payload")
public class ActivityLogResponse {

    @Schema(description = "Activity Log ID", example = "501")
    private Long id;

    @Schema(description = "Activity type", example = "ADMIN_ACTION")
    private ActivityType activityType;

    @Schema(description = "Module tag", example = "INVENTORY")
    private String module;

    @Schema(description = "Action description", example = "Adjusted stock quantity for Product #200")
    private String description;

    @Schema(description = "JSON metadata string", example = "{\"adjustedQuantity\":-5}")
    private String metadata;

    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Username", example = "admin@example.com")
    private String username;

    @Schema(description = "Client IP address", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "Client User-Agent", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "Is administrative activity flag", example = "true")
    private boolean isAdminActivity;

    @Schema(description = "Activity timestamp", example = "2026-08-05T14:30:00Z")
    private Instant timestamp;
}
