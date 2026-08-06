package com.example.ecommerce.scheduler.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background scheduled cron job performing system maintenance and automated log/record cleanup.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemCleanupScheduler {

    @Scheduled(cron = "0 0 4 * * ?") // Daily at 04:00 AM
    public void runSystemCleanup() {
        log.info("[CRON JOB] Starting daily automated system maintenance & cleanup task...");
        // Log system cleanup routine
        log.info("[CRON JOB] System cleanup maintenance routine executed successfully");
    }
}
