package com.example.ecommerce.logging.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for dynamically updating logger levels at runtime.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Logger level update request payload")
public class LogLevelUpdateRequest {

    @Schema(description = "Target logger package or category name", example = "com.example.ecommerce")
    @NotBlank(message = "Logger name is required")
    private String loggerName;

    @Schema(description = "Target log level (TRACE, DEBUG, INFO, WARN, ERROR, OFF)", example = "DEBUG")
    @NotBlank(message = "Log level is required")
    private String level;
}
