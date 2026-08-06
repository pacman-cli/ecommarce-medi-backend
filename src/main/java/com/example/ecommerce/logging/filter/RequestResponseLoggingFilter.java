package com.example.ecommerce.logging.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter logging HTTP request parameters, response status, latency,
 * and populating MDC tracing context (requestId, userId, clientIp).
 */
@Slf4j
@Component
public class RequestResponseLoggingFilter implements Filter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String MDC_REQUEST_ID = "requestId";
    private static final String MDC_CLIENT_IP = "clientIp";
    private static final String MDC_USER_ID = "userId";
    private static final String MDC_TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
            long startTime = System.currentTimeMillis();

            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

            String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
            if (!StringUtils.hasText(requestId)) {
                requestId = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            }

            String clientIp = extractClientIp(httpRequest);
            String userId = extractUserId();

            MDC.put(MDC_REQUEST_ID, requestId);
            MDC.put(MDC_CLIENT_IP, clientIp);
            MDC.put(MDC_USER_ID, userId);
            MDC.put(MDC_TRACE_ID, requestId);

            httpResponse.setHeader(REQUEST_ID_HEADER, requestId);

            String queryString = StringUtils.hasText(httpRequest.getQueryString()) ? "?" + httpRequest.getQueryString() : "";
            log.info("HTTP INCOMING REQUEST [{}] {} {}{} from IP: {}",
                    requestId, httpRequest.getMethod(), httpRequest.getRequestURI(), queryString, clientIp);

            try {
                filterChain.doFilter(wrappedRequest, wrappedResponse);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                int status = wrappedResponse.getStatus();

                log.info("HTTP OUTGOING RESPONSE [{}] {} {} Status: {} Duration: {} ms Content-Length: {} bytes",
                        requestId, httpRequest.getMethod(), httpRequest.getRequestURI(), status, duration, wrappedResponse.getContentSize());

                wrappedResponse.copyBodyToResponse();
                MDC.clear();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xfHeader)) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String extractUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "ANONYMOUS";
    }
}
