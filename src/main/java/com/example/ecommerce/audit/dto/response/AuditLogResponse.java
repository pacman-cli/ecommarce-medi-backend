package com.example.ecommerce.audit.dto.response;

import com.example.ecommerce.audit.dto.enums.AuditAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Entity audit log record response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity change audit log response payload")
public class AuditLogResponse {

    @Schema(description = "Audit Log ID", example = "1001")
    private Long id;

    @Schema(description = "Entity class name", example = "Product")
    private String entityName;

    @Schema(description = "Entity ID", example = "200")
    private String entityId;

    @Schema(description = "Audit action", example = "UPDATE")
    private AuditAction action;

    @Schema(description = "Old state JSON snapshot", example = "{\"price\":12.00}")
    private String oldState;

    @Schema(description = "New state JSON snapshot", example = "{\"price\":15.00}")
    private String newState;

    @Schema(description = "User ID auditor", example = "1")
    private Long userId;

    @Schema(description = "Username auditor", example = "admin")
    private String username;

    @Schema(description = "Client IP address", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "Client User-Agent", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "User who soft deleted entity", example = "admin@example.com")
    private String deletedBy;

    @Schema(description = "Audit timestamp", example = "2026-08-05T14:30:00Z")
    private Instant timestamp;
}
