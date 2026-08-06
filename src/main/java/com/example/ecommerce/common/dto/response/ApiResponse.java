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
 * Uniform response envelope for every successful and error REST endpoint response.
 *
 * @param <T> the payload type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Generic REST API response envelope")
public class ApiResponse<T> {

    @Schema(description = "Success status flag", example = "true")
    private boolean success;

    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Human-readable message description", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Response data payload")
    private T data;

    @Schema(description = "Optional response metadata object or map")
    private Map<String, Object> metadata;

    @Schema(description = "Timestamp when the response was generated", example = "2026-08-05T14:30:00Z")
    private Instant timestamp;

    @Schema(description = "Correlation trace ID", example = "REQ-8A2F119C")
    private String traceId;

    @Schema(description = "Error code identifier if applicable", example = "ERR_VALIDATION_FAILED")
    private String error;

    @Schema(description = "Field-level error details map if applicable")
    private Map<String, String> fieldErrors;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message, Map<String, Object> metadata) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .message(message)
                .data(data)
                .metadata(metadata)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return success(null, message);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode, int status) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .error(errorCode)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> validationError(Map<String, String> fieldErrors) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(400)
                .message("Request validation failed")
                .error("ERR_VALIDATION_FAILED")
                .fieldErrors(fieldErrors)
                .timestamp(Instant.now())
                .build();
    }
}
