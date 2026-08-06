package com.example.ecommerce.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Lightweight message response payload DTO for simple API notifications.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Simple API message response payload")
public class MessageResponse {

    @Schema(description = "Message content", example = "Password reset instructions sent to your email")
    private String message;

    @Schema(description = "Timestamp", example = "2026-08-05T14:30:00Z")
    @Builder.Default
    private Instant timestamp = Instant.now();

    public static MessageResponse of(String message) {
        return MessageResponse.builder()
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
}
