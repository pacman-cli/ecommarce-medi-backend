package com.example.ecommerce.logging.service.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.logging.dto.request.LogLevelUpdateRequest;
import com.example.ecommerce.logging.dto.response.LoggerInfoResponse;
import com.example.ecommerce.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation leveraging Logback {@link LoggerContext} to inspect
 * and dynamically adjust logger levels at runtime.
 */
@Slf4j
@Service
public class LoggingServiceImpl implements LoggingService {

    private LoggerContext getLoggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @Override
    public List<LoggerInfoResponse> getAllLoggers() {
        LoggerContext context = getLoggerContext();
        return context.getLoggerList().stream()
                .filter(l -> l.getLevel() != null || l.getName().startsWith("com.example.ecommerce"))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LoggerInfoResponse getLoggerByName(String name) {
        LoggerContext context = getLoggerContext();
        Logger logger = context.exists(name);
        if (logger == null) {
            throw new ResourceNotFoundException("Logger", "name", name);
        }
        return mapToResponse(logger);
    }

    @Override
    public LoggerInfoResponse updateLogLevel(LogLevelUpdateRequest request) {
        log.info("Updating logger level for '{}' to '{}'", request.getLoggerName(), request.getLevel());
        LoggerContext context = getLoggerContext();
        Logger logger = context.getLogger(request.getLoggerName());

        if (logger == null) {
            throw new ResourceNotFoundException("Logger", "name", request.getLoggerName());
        }

        Level targetLevel = Level.toLevel(request.getLevel().toUpperCase(), null);
        if (targetLevel == null && !"OFF".equalsIgnoreCase(request.getLevel())) {
            throw new BadRequestException("Invalid log level: " + request.getLevel() + ". Allowed: TRACE, DEBUG, INFO, WARN, ERROR, OFF");
        }

        logger.setLevel(targetLevel);
        log.info("Successfully updated logger '{}' level to '{}'", logger.getName(), targetLevel);

        return mapToResponse(logger);
    }

    private LoggerInfoResponse mapToResponse(Logger logger) {
        return LoggerInfoResponse.builder()
                .name(logger.getName())
                .configuredLevel(logger.getLevel() != null ? logger.getLevel().toString() : "INHERITED")
                .effectiveLevel(logger.getEffectiveLevel().toString())
                .build();
    }
}
