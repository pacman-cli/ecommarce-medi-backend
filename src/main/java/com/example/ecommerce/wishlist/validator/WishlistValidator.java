package com.example.ecommerce.wishlist.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.wishlist.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates product availability and duplication before saving to wishlist.
 */
@Component
@RequiredArgsConstructor
public class WishlistValidator {

    private final WishlistItemRepository wishlistItemRepository;

    public void validateProductForWishlist(Long userId, Product product) {
        if (product == null || product.isDeleted()) {
            throw new BadRequestException("Product is unavailable or no longer exists");
        }
        if (!product.isActive() || product.getStatus() != ProductStatus.ACTIVE) {
            throw new BadRequestException("Product '" + product.getName() + "' is inactive and cannot be added to wishlist");
        }
        boolean exists = wishlistItemRepository.existsByWishlistUserIdAndProductId(userId, product.getId());
        if (exists) {
            throw new ConflictException("Product '" + product.getName() + "' is already in your wishlist");
        }
    }
}
