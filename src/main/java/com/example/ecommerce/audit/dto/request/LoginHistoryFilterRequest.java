package com.example.ecommerce.audit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

/**
 * Filter criteria payload for querying authentication login histories.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login history query filter criteria payload")
public class LoginHistoryFilterRequest {

    @Schema(description = "User email search filter", example = "admin@example.com")
    private String userEmail;

    @Schema(description = "User ID filter", example = "1")
    private Long userId;

    @Schema(description = "Success status filter", example = "false")
    private Boolean success;

    @Schema(description = "IP address search filter", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "Start timestamp bound", example = "2026-08-01T00:00:00Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant startDate;

    @Schema(description = "End timestamp bound", example = "2026-08-31T23:59:59Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant endDate;
}
