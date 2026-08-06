package com.example.ecommerce.cart.repository;

import com.example.ecommerce.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access repository for {@link CartItem} line items.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    void deleteByCartId(Long cartId);
}
