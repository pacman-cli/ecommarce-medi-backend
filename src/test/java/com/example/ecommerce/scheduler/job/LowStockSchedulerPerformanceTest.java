package com.example.ecommerce.scheduler.job;

import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class LowStockSchedulerPerformanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LowStockScheduler scheduler;

    @MockBean
    private CacheManager cacheManager;

    @Test
    public void testPerformance() {
        productRepository.deleteAllInBatch();

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            Product product = new Product();
            product.setName("Test Product " + i);
            product.setSku("TEST-SKU-" + i);
            product.setSlug("test-product-" + i);
            product.setSellingPrice(new java.math.BigDecimal("10.0"));
            product.setDeleted(false);

            // make 10% low stock
            if (i % 10 == 0) {
                product.setQuantity(2);
                product.setLowStock(5);
                product.setStockStatus(StockStatus.LOW_STOCK);
            } else {
                product.setQuantity(100);
                product.setLowStock(5);
                product.setStockStatus(StockStatus.IN_STOCK);
            }
            products.add(product);

            if(i % 1000 == 0) {
                productRepository.saveAllAndFlush(products);
                products.clear();
            }
        }
        if(!products.isEmpty()) {
            productRepository.saveAllAndFlush(products);
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        scheduler.runLowStockCheck();

        stopWatch.stop();
        System.out.println("====== LOW STOCK BASELINE TIME TAKEN: " + stopWatch.getTotalTimeMillis() + " ms ======");
    }
}
