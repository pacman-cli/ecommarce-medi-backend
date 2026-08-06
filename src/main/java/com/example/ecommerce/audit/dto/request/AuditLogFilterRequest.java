package com.example.ecommerce.audit.dto.request;

import com.example.ecommerce.audit.dto.enums.AuditAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

/**
 * Filter criteria payload for querying entity audit logs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Audit log query filter criteria payload")
public class AuditLogFilterRequest {

    @Schema(description = "Entity class name filter", example = "Product")
    private String entityName;

    @Schema(description = "Entity ID filter", example = "200")
    private String entityId;

    @Schema(description = "Audit action filter", example = "UPDATE")
    private AuditAction action;

    @Schema(description = "User ID auditor filter", example = "1")
    private Long userId;

    @Schema(description = "Username auditor filter", example = "admin")
    private String username;

    @Schema(description = "Start timestamp bound", example = "2026-08-01T00:00:00Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant startDate;

    @Schema(description = "End timestamp bound", example = "2026-08-31T23:59:59Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant endDate;
}
