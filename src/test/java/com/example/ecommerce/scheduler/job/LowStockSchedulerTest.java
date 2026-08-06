package com.example.ecommerce.scheduler.job;

import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LowStockSchedulerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private LowStockScheduler lowStockScheduler;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .name("Napa 500mg")
                .sku("MED-NAPA-500")
                .quantity(2)
                .reservedQuantity(0)
                .lowStock(5)
                .stockStatus(StockStatus.LOW_STOCK)
                .build();
        sampleProduct.setId(200L);
    }

    @Test
    void testRunLowStockCheckSuccess() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(sampleProduct));

        lowStockScheduler.runLowStockCheck();

        verify(productRepository, times(1)).findAll();
    }
}
