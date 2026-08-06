package com.example.ecommerce.product.repository;

import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("Paracetamol 500mg")
                .slug("paracetamol-500mg")
                .sku("MED-PARA-500")
                .sellingPrice(new BigDecimal("10.00"))
                .costPrice(new BigDecimal("5.00"))
                .quantity(100)
                .lowStock(10)
                .status(ProductStatus.ACTIVE)
                .stockStatus(StockStatus.IN_STOCK)
                .build();
    }

    @Test
    void testSaveAndFindById() {
        Product saved = productRepository.save(product);
        assertNotNull(saved.getId());

        Optional<Product> found = productRepository.findByIdAndDeletedFalse(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("MED-PARA-500", found.get().getSku());
    }

    @Test
    void testExistsBySkuIgnoreCase() {
        productRepository.save(product);

        boolean exists = productRepository.existsBySkuIgnoreCase("med-para-500");
        assertTrue(exists);
    }

    @Test
    void testSoftDeleteExclusion() {
        Product saved = productRepository.save(product);
        saved.setDeleted(true);
        productRepository.save(saved);

        Optional<Product> found = productRepository.findByIdAndDeletedFalse(saved.getId());
        assertFalse(found.isPresent());
    }
}
