package com.example.ecommerce.wishlist.service;

import com.example.ecommerce.cart.dto.response.CartResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.wishlist.dto.request.WishlistFilterRequest;
import com.example.ecommerce.wishlist.dto.response.WishlistCountResponse;
import com.example.ecommerce.wishlist.dto.response.WishlistItemResponse;
import com.example.ecommerce.wishlist.dto.response.WishlistResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface defining operations for wishlist management, move-to-cart actions,
 * recently added lookups and count statistics.
 */
public interface WishlistService {

    WishlistResponse getWishlist();

    WishlistResponse addProductToWishlist(Long productId);

    WishlistResponse removeProductFromWishlist(Long productId);

    CartResponse moveToCart(Long productId, String sessionId);

    WishlistCountResponse getWishlistCount();

    List<WishlistItemResponse> getRecentlyAdded(int limit);

    PageResponse<WishlistItemResponse> getWishlistItems(WishlistFilterRequest filter, Pageable pageable);
}
