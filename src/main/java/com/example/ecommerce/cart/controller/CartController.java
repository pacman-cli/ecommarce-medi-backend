package com.example.ecommerce.cart.controller;

import com.example.ecommerce.cart.dto.request.AddToCartRequest;
import com.example.ecommerce.cart.dto.request.ApplyCouponRequest;
import com.example.ecommerce.cart.dto.request.MergeCartRequest;
import com.example.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.cart.dto.response.CartResponse;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing endpoints for guest & user shopping cart operations,
 * line item modifications, stock validations, coupon code applications, and cart merging.
 */
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Management", description = "Endpoints for guest & user cart management, line item updates, coupons and cart merging")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get active cart", description = "Retrieves active shopping cart for authenticated user or guest session")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    })
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @Parameter(description = "Guest Session ID header (if unauthenticated)") @RequestHeader(value = "X-Session-ID", required = false) String headerSessionId,
            @Parameter(description = "Guest Session ID query parameter") @RequestParam(value = "sessionId", required = false) String paramSessionId) {
        String sessionId = headerSessionId != null ? headerSessionId : paramSessionId;
        CartResponse cart = cartService.getOrCreateCart(sessionId);
        return ResponseEntity.ok(ApiResponse.success(cart, "Cart retrieved successfully"));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Adds a product line item to cart with stock and active status validation")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Item added to cart successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Product out of stock, inactive or insufficient stock quantity")
    })
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @Valid @RequestBody AddToCartRequest request) {
        CartResponse updatedCart = cartService.addToCart(request);
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Item added to cart successfully"));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update cart item quantity", description = "Updates quantity of a line item in cart")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Stock limit exceeded or invalid quantity"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @Parameter(description = "Cart item ID", required = true) @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        CartResponse updatedCart = cartService.updateCartItem(itemId, request, sessionId);
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Cart item updated successfully"));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove item from cart", description = "Removes a specific line item from cart")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Item removed from cart successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    public ResponseEntity<ApiResponse<CartResponse>> removeCartItem(
            @Parameter(description = "Cart item ID", required = true) @PathVariable Long itemId,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        CartResponse updatedCart = cartService.removeCartItem(itemId, sessionId);
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Item removed from cart successfully"));
    }

    @PostMapping("/coupon")
    @Operation(summary = "Apply coupon code", description = "Applies a promotional discount coupon code to cart")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coupon applied successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired coupon code")
    })
    public ResponseEntity<ApiResponse<CartResponse>> applyCoupon(
            @Valid @RequestBody ApplyCouponRequest request,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        CartResponse updatedCart = cartService.applyCoupon(request, sessionId);
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Coupon applied successfully"));
    }

    @DeleteMapping("/coupon")
    @Operation(summary = "Remove coupon code", description = "Removes applied promo coupon code from cart")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Coupon removed successfully")
    })
    public ResponseEntity<ApiResponse<CartResponse>> removeCoupon(
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        CartResponse updatedCart = cartService.removeCoupon(sessionId);
        return ResponseEntity.ok(ApiResponse.success(updatedCart, "Coupon removed successfully"));
    }

    @DeleteMapping
    @Operation(summary = "Clear cart", description = "Removes all items and coupons from the shopping cart")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    })
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        CartResponse clearedCart = cartService.clearCart(sessionId);
        return ResponseEntity.ok(ApiResponse.success(clearedCart, "Cart cleared successfully"));
    }

    @PostMapping("/merge")
    @Operation(summary = "Merge guest cart", description = "Merges a guest session cart into authenticated user cart upon login")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Guest cart merged successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Unauthenticated or invalid guest session ID")
    })
    public ResponseEntity<ApiResponse<CartResponse>> mergeGuestCart(
            @Valid @RequestBody MergeCartRequest request) {
        CartResponse mergedCart = cartService.mergeGuestCart(request);
        return ResponseEntity.ok(ApiResponse.success(mergedCart, "Guest cart merged successfully"));
    }
}
