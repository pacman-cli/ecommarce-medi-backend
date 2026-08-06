package com.example.ecommerce.security;

import com.example.ecommerce.common.constant.AppConstants;
import com.example.ecommerce.common.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Emits a JSON {@link ErrorResponse} with {@code 403} whenever an authenticated
 * user attempts an action they are not authorized to perform.
 */
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * Writes the 403 JSON response.
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpServletResponse.SC_FORBIDDEN)
                .error("Forbidden")
                .message("You do not have permission to perform this action")
                .path(request.getRequestURI())
                .traceId(MDC.get(AppConstants.TRACE_ID_MDC_KEY))
                .build();
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
