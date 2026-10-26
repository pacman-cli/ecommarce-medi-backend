package com.example.ecommerce.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Background scheduled job to keep the Render server alive by pinging its own health endpoint.
 * Render shuts down free tier instances after 15 minutes of inactivity.
 * This runs every 10 minutes to prevent shutdown.
 */
@Slf4j
@Component
public class RenderKeepAliveScheduler {

    private final RestTemplate restTemplate;

    // STORAGE_BASE_URL evaluates to ${RENDER_EXTERNAL_URL} on Render
    @Value("${app.storage.base-url:}")
    private String baseUrl;

    public RenderKeepAliveScheduler(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    // Run every 10 minutes (600000 milliseconds)
    @Scheduled(fixedRate = 600000)
    public void keepAlive() {
        if (baseUrl != null && !baseUrl.isEmpty() && !baseUrl.contains("localhost")) {
            try {
                // If it is just the domain or has /api/v1 at the end, we extract the root
                String rootUrl = baseUrl;
                if (rootUrl.endsWith("/api/v1")) {
                    rootUrl = rootUrl.substring(0, rootUrl.length() - 7);
                } else if (rootUrl.endsWith("/api/v1/")) {
                    rootUrl = rootUrl.substring(0, rootUrl.length() - 8);
                }

                String healthUrl = rootUrl + (rootUrl.endsWith("/") ? "" : "/") + "actuator/health";
                log.info("[KEEP ALIVE] Pinging server at {} to prevent Render sleep...", healthUrl);
                restTemplate.getForObject(healthUrl, String.class);
                log.info("[KEEP ALIVE] Ping successful.");
            } catch (Exception e) {
                log.warn("[KEEP ALIVE] Ping failed: {}", e.getMessage());
            }
        } else {
            log.debug("[KEEP ALIVE] Base URL is localhost or empty, skipping keep-alive ping.");
        }
    }
}
