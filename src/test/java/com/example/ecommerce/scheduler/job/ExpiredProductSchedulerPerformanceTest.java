package com.example.ecommerce.scheduler.job;

import com.example.ecommerce.inventory.entity.BatchStatus;
import com.example.ecommerce.inventory.entity.StockBatch;
import com.example.ecommerce.inventory.repository.StockBatchRepository;
import com.example.ecommerce.inventory.entity.Warehouse;
import com.example.ecommerce.inventory.repository.WarehouseRepository;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class ExpiredProductSchedulerPerformanceTest {

    @Autowired
    private StockBatchRepository stockBatchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ExpiredProductScheduler scheduler;

    @MockBean
    private CacheManager cacheManager;

    @Test
    public void testPerformance() {
        stockBatchRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        warehouseRepository.deleteAllInBatch();

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Main Warehouse");
        warehouse.setCode("WH-MAIN");
        warehouse = warehouseRepository.saveAndFlush(warehouse);

        Product product = new Product();
        product.setName("Test Product");
        product.setSku("TEST-SKU");
        product.setSlug("test-product");
        product.setSellingPrice(new BigDecimal("10.0"));
        product.setDeleted(false);
        product = productRepository.saveAndFlush(product);

        List<StockBatch> batches = new ArrayList<>();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        for (int i = 0; i < 2000; i++) {
            StockBatch batch = new StockBatch();
            batch.setBatchNumber("BATCH-" + i);
            batch.setStatus(BatchStatus.AVAILABLE);
            batch.setExpiryDate(yesterday);
            batch.setDeleted(false);
            batch.setProduct(product);
            batch.setWarehouse(warehouse);
            batch.setQuantity(10);
            batch.setAvailableQuantity(10);
            batches.add(batch);
        }
        stockBatchRepository.saveAllAndFlush(batches);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        scheduler.runExpiredProductCheck();

        stopWatch.stop();
        System.out.println("====== BASELINE TIME TAKEN: " + stopWatch.getTotalTimeMillis() + " ms ======");
    }
}
