package com.example.ecommerce.audit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * System audit activity summary metrics DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "System audit metrics summary payload")
public class AuditSummaryResponse {

    @Schema(description = "Total recorded entity audit logs count", example = "1250")
    private Long totalAuditLogs;

    @Schema(description = "Total recorded activity logs count", example = "3400")
    private Long totalActivityLogs;

    @Schema(description = "Total recorded admin activities count", example = "450")
    private Long totalAdminActivities;

    @Schema(description = "Total recorded login attempts count", example = "8900")
    private Long totalLoginAttempts;

    @Schema(description = "Total failed login attempts count", example = "42")
    private Long failedLoginAttempts;
}
