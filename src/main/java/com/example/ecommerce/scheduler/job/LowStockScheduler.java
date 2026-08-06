package com.example.ecommerce.scheduler.job;

import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Background scheduled cron job monitoring product inventory stock levels.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LowStockScheduler {

    private final ProductRepository productRepository;

    @Scheduled(cron = "0 0 */2 * * ?") // Every 2 hours
    public void runLowStockCheck() {
        log.info("[CRON JOB] Starting low stock inventory monitoring check...");
        List<Product> products = productRepository.findAll();

        int lowStockCount = 0;
        for (Product product : products) {
            if (!product.isDeleted()) {
                int qty = product.getQuantity() != null ? product.getQuantity() : 0;
                int threshold = product.getLowStock() != null ? product.getLowStock() : 5;

                if (qty <= threshold || product.getStockStatus() == StockStatus.LOW_STOCK) {
                    lowStockCount++;
                    log.warn("[LOW STOCK ALERT] Product ID: {} ({}) SKU: {} has low stock balance! Current Qty: {}, Threshold: {}",
                            product.getId(), product.getName(), product.getSku(), qty, threshold);
                }
            }
        }

        log.info("[CRON JOB] Low stock check completed. Total low stock products flagged: {}", lowStockCount);
    }
}
