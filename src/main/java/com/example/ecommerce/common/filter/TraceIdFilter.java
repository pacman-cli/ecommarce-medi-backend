package com.example.ecommerce.common.filter;

import com.example.ecommerce.common.constant.AppConstants;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * Generates (or forwards) a request trace identifier and exposes it via the
 * SLF4J MDC so that every log statement of a single request can be correlated.
 *
 * <p>The trace id is also echoed back through the {@code X-Trace-Id} response
 * header so clients can reference the exact log trail when reporting issues.</p>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
            String traceId = httpRequest.getHeader(AppConstants.TRACE_ID_HEADER);
            if (!StringUtils.hasText(traceId)) {
                traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            }
            MDC.put(AppConstants.TRACE_ID_MDC_KEY, traceId);
            httpResponse.setHeader(AppConstants.TRACE_ID_HEADER, traceId);
            try {
                chain.doFilter(request, response);
            } finally {
                MDC.remove(AppConstants.TRACE_ID_MDC_KEY);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
