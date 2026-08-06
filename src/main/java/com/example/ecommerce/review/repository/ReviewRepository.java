package com.example.ecommerce.review.repository;

import com.example.ecommerce.review.entity.Review;
import com.example.ecommerce.review.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access repository for {@link Review} entities and rating aggregates.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    Optional<Review> findByIdAndDeletedFalse(Long id);

    boolean existsByProductIdAndUserIdAndDeletedFalse(Long productId, Long userId);

    long countByProductIdAndStatusAndDeletedFalse(Long productId, ReviewStatus status);

    long countByProductIdAndRatingAndStatusAndDeletedFalse(Long productId, Integer rating, ReviewStatus status);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId AND r.status = :status AND r.deleted = false")
    Double getAverageRatingByProductId(@Param("productId") Long productId, @Param("status") ReviewStatus status);
}
