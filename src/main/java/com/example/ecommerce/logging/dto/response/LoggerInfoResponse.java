package com.example.ecommerce.logging.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Logger configuration state response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Logger configuration details payload")
public class LoggerInfoResponse {

    @Schema(description = "Logger category/package name", example = "com.example.ecommerce")
    private String name;

    @Schema(description = "Configured log level", example = "INFO")
    private String configuredLevel;

    @Schema(description = "Effective log level inherited from parent", example = "INFO")
    private String effectiveLevel;
}
