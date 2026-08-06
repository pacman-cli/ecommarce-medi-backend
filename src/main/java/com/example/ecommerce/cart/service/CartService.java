package com.example.ecommerce.cart.service;

import com.example.ecommerce.cart.dto.request.AddToCartRequest;
import com.example.ecommerce.cart.dto.request.ApplyCouponRequest;
import com.example.ecommerce.cart.dto.request.MergeCartRequest;
import com.example.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.cart.dto.response.CartResponse;

/**
 * Service interface for shopping cart operations, guest/user session management,
 * coupon applications, stock validations, and guest cart merging.
 */
public interface CartService {

    /**
     * Retrieves active cart for authenticated user or guest session.
     *
     * @param sessionId optional guest session ID
     * @return active cart response DTO
     */
    CartResponse getOrCreateCart(String sessionId);

    /**
     * Adds an item to the shopping cart.
     *
     * @param request add to cart payload
     * @return updated cart response DTO
     */
    CartResponse addToCart(AddToCartRequest request);

    /**
     * Updates quantity of a line item.
     *
     * @param itemId    cart item ID
     * @param request   update payload
     * @param sessionId guest session ID
     * @return updated cart response DTO
     */
    CartResponse updateCartItem(Long itemId, UpdateCartItemRequest request, String sessionId);

    /**
     * Removes an item from the cart.
     *
     * @param itemId    cart item ID
     * @param sessionId guest session ID
     * @return updated cart response DTO
     */
    CartResponse removeCartItem(Long itemId, String sessionId);

    /**
     * Applies a promotional coupon code.
     *
     * @param request   coupon payload
     * @param sessionId guest session ID
     * @return updated cart response DTO
     */
    CartResponse applyCoupon(ApplyCouponRequest request, String sessionId);

    /**
     * Removes applied coupon code.
     *
     * @param sessionId guest session ID
     * @return updated cart response DTO
     */
    CartResponse removeCoupon(String sessionId);

    /**
     * Clears all items from the cart.
     *
     * @param sessionId guest session ID
     * @return empty cart response DTO
     */
    CartResponse clearCart(String sessionId);

    /**
     * Merges a guest session cart into an authenticated user's cart upon login.
     *
     * @param request merge payload
     * @return merged cart response DTO
     */
    CartResponse mergeGuestCart(MergeCartRequest request);
}
