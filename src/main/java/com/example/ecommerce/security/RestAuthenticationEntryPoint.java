package com.example.ecommerce.security;

import com.example.ecommerce.common.constant.AppConstants;
import com.example.ecommerce.common.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Emits a JSON {@link ErrorResponse} with {@code 401} instead of Spring's default
 * HTML page whenever an unauthenticated request reaches a protected endpoint.
 */
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * Writes the 401 JSON response.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Unauthorized")
                .message("Full authentication is required to access this resource")
                .path(request.getRequestURI())
                .traceId(MDC.get(AppConstants.TRACE_ID_MDC_KEY))
                .build();
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
