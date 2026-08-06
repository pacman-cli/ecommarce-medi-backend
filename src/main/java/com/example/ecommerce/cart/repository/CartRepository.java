package com.example.ecommerce.cart.repository;

import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.entity.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access repository for {@link Cart} entities.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndStatus(Long userId, CartStatus status);

    Optional<Cart> findBySessionIdAndStatus(String sessionId, CartStatus status);

    Optional<Cart> findByIdAndStatus(Long id, CartStatus status);
}
