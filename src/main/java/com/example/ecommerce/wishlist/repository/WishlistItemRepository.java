package com.example.ecommerce.wishlist.repository;

import com.example.ecommerce.wishlist.entity.WishlistItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link WishlistItem} entries.
 */
@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long>, JpaSpecificationExecutor<WishlistItem> {

    Optional<WishlistItem> findByWishlistUserIdAndProductId(Long userId, Long productId);

    boolean existsByWishlistUserIdAndProductId(Long userId, Long productId);

    long countByWishlistUserId(Long userId);

    List<WishlistItem> findByWishlistUserIdOrderByAddedAtDesc(Long userId, Pageable pageable);

    void deleteByWishlistUserIdAndProductId(Long userId, Long productId);
}
