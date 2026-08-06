package com.example.ecommerce.wishlist.repository;

import com.example.ecommerce.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access repository for {@link Wishlist} aggregates.
 */
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserId(Long userId);
}
