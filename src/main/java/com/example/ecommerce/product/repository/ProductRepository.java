package com.example.ecommerce.product.repository;

import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link Product} entities with specification execution support.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Finds a non-deleted product by ID.
     */
    Optional<Product> findByIdAndDeletedFalse(Long id);

    /**
     * Finds a product by SKU.
     */
    Optional<Product> findBySkuAndDeletedFalse(String sku);

    /**
     * Finds a product by URL slug.
     */
    Optional<Product> findBySlug(String slug);

    /**
     * Finds a non-deleted product by URL slug.
     */
    Optional<Product> findBySlugAndDeletedFalse(String slug);

    /**
     * Checks if a product name already exists (case-insensitive).
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Checks if a product name exists excluding a specific ID.
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    /**
     * Checks if a product SKU exists (case-insensitive).
     */
    boolean existsBySkuIgnoreCase(String sku);

    /**
     * Checks if a product SKU exists excluding a specific ID.
     */
    boolean existsBySkuIgnoreCaseAndIdNot(String sku, Long id);

    /**
     * Checks if a product slug exists (case-insensitive).
     */
    boolean existsBySlugIgnoreCase(String slug);

    /**
     * Checks if a product slug exists excluding a specific ID.
     */
    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);

    /**
     * Checks if a product barcode exists (case-insensitive).
     */
    boolean existsByBarcodeIgnoreCase(String barcode);

    /**
     * Checks if a product barcode exists excluding a specific ID.
     */
    boolean existsByBarcodeIgnoreCaseAndIdNot(String barcode, Long id);

    /**
     * Finds featured products by status.
     */
    List<Product> findByFeaturedTrueAndStatusAndDeletedFalse(ProductStatus status);

    /**
     * Finds bestseller products by status.
     */
    List<Product> findByBestsellerTrueAndStatusAndDeletedFalse(ProductStatus status);

    /**
     * Finds new arrival products by status.
     */
    List<Product> findByNewArrivalTrueAndStatusAndDeletedFalse(ProductStatus status);

    /**
     * Finds trending products by status.
     */
    List<Product> findByTrendingTrueAndStatusAndDeletedFalse(ProductStatus status);

    /**
     * Finds recommended products by status.
     */
    List<Product> findByRecommendedTrueAndStatusAndDeletedFalse(ProductStatus status);
}
