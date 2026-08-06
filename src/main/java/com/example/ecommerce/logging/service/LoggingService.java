package com.example.ecommerce.logging.service;

import com.example.ecommerce.logging.dto.request.LogLevelUpdateRequest;
import com.example.ecommerce.logging.dto.response.LoggerInfoResponse;

import java.util.List;

/**
 * Service interface defining operations for inspecting and dynamically managing
 * SLF4J Logback logger levels at runtime.
 */
public interface LoggingService {

    /**
     * Retrieves all active configured loggers and their effective levels.
     */
    List<LoggerInfoResponse> getAllLoggers();

    /**
     * Retrieves logger configuration for specific category name.
     */
    LoggerInfoResponse getLoggerByName(String name);

    /**
     * Dynamically updates log level for specified logger category.
     */
    LoggerInfoResponse updateLogLevel(LogLevelUpdateRequest request);
}
