package com.example.ecommerce.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Uniform error contract returned by the global exception handler and the
 * security authentication/authorization entry points.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Standard API error response payload")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2026-08-05T14:30:00Z")
    private Instant timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "HTTP status phrase", example = "Not Found")
    private String error;

    @Schema(description = "Machine-readable error code identifier", example = "RESOURCE_NOT_FOUND")
    private String errorCode;

    @Schema(description = "Human-readable error description message", example = "Product with ID '200' not found")
    private String message;

    @Schema(description = "Request URI path where error occurred", example = "/api/v1/products/200")
    private String path;

    @Schema(description = "Correlation trace ID for request tracking", example = "REQ-8A2F119C")
    private String traceId;

    @Schema(description = "Field-level validation error messages map (for 400 Bad Request)")
    private Map<String, String> fieldErrors;
}
