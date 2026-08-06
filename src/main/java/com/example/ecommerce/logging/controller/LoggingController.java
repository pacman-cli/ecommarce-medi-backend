package com.example.ecommerce.logging.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.logging.dto.request.LogLevelUpdateRequest;
import com.example.ecommerce.logging.dto.response.LoggerInfoResponse;
import com.example.ecommerce.logging.service.LoggingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for inspecting active Logback logger configurations
 * and dynamically updating log levels at runtime.
 */
@RestController
@RequestMapping("/api/v1/logging")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Logging Management", description = "Endpoints for inspecting and dynamically adjusting application log levels at runtime")
public class LoggingController {

    private final LoggingService loggingService;

    @GetMapping("/levels")
    @Operation(summary = "Get all active loggers", description = "Retrieves a list of all configured loggers and their effective log levels")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active loggers retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<LoggerInfoResponse>>> getAllLoggers() {
        List<LoggerInfoResponse> loggers = loggingService.getAllLoggers();
        return ResponseEntity.ok(ApiResponse.success(loggers, "Loggers retrieved successfully"));
    }

    @GetMapping("/levels/{name}")
    @Operation(summary = "Get logger level by name", description = "Retrieves configuration details for a specific logger category name")
    public ResponseEntity<ApiResponse<LoggerInfoResponse>> getLoggerByName(
            @Parameter(description = "Logger name/category", required = true) @PathVariable String name) {
        LoggerInfoResponse logger = loggingService.getLoggerByName(name);
        return ResponseEntity.ok(ApiResponse.success(logger, "Logger details retrieved successfully"));
    }

    @PutMapping("/levels")
    @Operation(summary = "Update logger level at runtime", description = "Dynamically changes the log level (TRACE, DEBUG, INFO, WARN, ERROR, OFF) for a package/logger without server restart")
    public ResponseEntity<ApiResponse<LoggerInfoResponse>> updateLogLevel(
            @Valid @RequestBody LogLevelUpdateRequest request) {
        LoggerInfoResponse updated = loggingService.updateLogLevel(request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Logger level updated successfully"));
    }
}
