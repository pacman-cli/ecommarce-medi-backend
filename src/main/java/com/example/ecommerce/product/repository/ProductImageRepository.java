package com.example.ecommerce.product.repository;

import com.example.ecommerce.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link ProductImage} gallery items.
 */
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    /**
     * Returns list of images belonging to a product sorted by display order.
     */
    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(Long productId);

    /**
     * Deletes all gallery images associated with a product ID.
     */
    void deleteByProductId(Long productId);
}
