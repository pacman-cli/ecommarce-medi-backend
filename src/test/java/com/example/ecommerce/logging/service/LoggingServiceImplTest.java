package com.example.ecommerce.logging.service;

import com.example.ecommerce.logging.dto.request.LogLevelUpdateRequest;
import com.example.ecommerce.logging.dto.response.LoggerInfoResponse;
import com.example.ecommerce.logging.service.impl.LoggingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoggingServiceImplTest {

    private LoggingServiceImpl loggingService;

    @BeforeEach
    void setUp() {
        loggingService = new LoggingServiceImpl();
    }

    @Test
    void testGetAllLoggersReturnsList() {
        List<LoggerInfoResponse> loggers = loggingService.getAllLoggers();
        assertNotNull(loggers);
        assertFalse(loggers.isEmpty());
    }

    @Test
    void testUpdateLogLevelSuccess() {
        LogLevelUpdateRequest request = LogLevelUpdateRequest.builder()
                .loggerName("com.example.ecommerce")
                .level("DEBUG")
                .build();

        LoggerInfoResponse response = loggingService.updateLogLevel(request);
        assertNotNull(response);
        assertEquals("com.example.ecommerce", response.getName());
        assertEquals("DEBUG", response.getConfiguredLevel());
    }
}
