package com.example.ecommerce.scheduler.job;

import com.example.ecommerce.inventory.entity.BatchStatus;
import com.example.ecommerce.inventory.entity.StockBatch;
import com.example.ecommerce.inventory.repository.StockBatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Background scheduled cron job marking inventory stock batches as EXPIRED past their expiration date.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredProductScheduler {

    private final StockBatchRepository stockBatchRepository;

    @Scheduled(cron = "0 45 0 * * ?") // Daily at 00:45 AM
    @Transactional
    public void runExpiredProductCheck() {
        log.info("[CRON JOB] Starting expired inventory stock batch check...");
        LocalDate today = LocalDate.now();
        int count = 0;

        for (StockBatch batch : stockBatchRepository.findAll()) {
            if (!batch.isDeleted() && batch.getStatus() != BatchStatus.EXPIRED && batch.getExpiryDate() != null && batch.getExpiryDate().isBefore(today)) {
                batch.setStatus(BatchStatus.EXPIRED);
                stockBatchRepository.save(batch);
                count++;
                log.warn("[BATCH EXPIRED ALERT] Stock batch LOT #: {} ID: {} for product ID: {} expired on {}",
                        batch.getBatchNumber(), batch.getId(), batch.getProduct().getId(), batch.getExpiryDate());
            }
        }

        log.info("[CRON JOB] Expired stock batch check completed. Total batches marked EXPIRED: {}", count);
    }
}
