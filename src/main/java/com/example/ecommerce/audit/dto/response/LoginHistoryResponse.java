package com.example.ecommerce.audit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Login history log response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication login history record response payload")
public class LoginHistoryResponse {

    @Schema(description = "Login History ID", example = "201")
    private Long id;

    @Schema(description = "User email address", example = "john.doe@example.com")
    private String userEmail;

    @Schema(description = "User ID (if authenticated)", example = "5")
    private Long userId;

    @Schema(description = "Authentication success flag", example = "true")
    private boolean success;

    @Schema(description = "Failure reason if failed", example = "Invalid password credential")
    private String failureReason;

    @Schema(description = "Client IP address", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "Client User-Agent", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "Geographic location estimate", example = "Dhaka, Bangladesh")
    private String location;

    @Schema(description = "Login attempt timestamp", example = "2026-08-05T14:30:00Z")
    private Instant timestamp;
}
