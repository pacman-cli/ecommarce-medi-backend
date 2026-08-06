package com.example.ecommerce.wishlist.controller;

import com.example.ecommerce.cart.dto.response.CartResponse;
import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.wishlist.dto.request.WishlistFilterRequest;
import com.example.ecommerce.wishlist.dto.response.WishlistCountResponse;
import com.example.ecommerce.wishlist.dto.response.WishlistItemResponse;
import com.example.ecommerce.wishlist.dto.response.WishlistResponse;
import com.example.ecommerce.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing endpoints for user wishlist management, product saved items,
 * move-to-cart actions, count statistics and recently added items.
 */
@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Wishlist Management", description = "Endpoints for user saved wishlist items, move-to-cart operations, count and recently added products")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    @Operation(summary = "Get user wishlist", description = "Retrieves full user wishlist or paginated items with dynamic filtering")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wishlist retrieved successfully")
    })
    public ResponseEntity<ApiResponse<WishlistResponse>> getWishlist() {
        WishlistResponse wishlist = wishlistService.getWishlist();
        return ResponseEntity.ok(ApiResponse.success(wishlist, "Wishlist retrieved successfully"));
    }

    @GetMapping("/items")
    @Operation(summary = "Get paginated wishlist items", description = "Returns a paginated list of wishlist items based on search and category/brand filters")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wishlist items retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<WishlistItemResponse>>> getWishlistItems(
            @ModelAttribute WishlistFilterRequest filter,
            @PageableDefault(sort = "addedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<WishlistItemResponse> page = wishlistService.getWishlistItems(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Wishlist items retrieved successfully"));
    }

    @GetMapping("/count")
    @Operation(summary = "Get wishlist count", description = "Returns total number of items saved in user's wishlist")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wishlist count retrieved successfully")
    })
    public ResponseEntity<ApiResponse<WishlistCountResponse>> getWishlistCount() {
        WishlistCountResponse count = wishlistService.getWishlistCount();
        return ResponseEntity.ok(ApiResponse.success(count, "Wishlist count retrieved successfully"));
    }

    @GetMapping("/recently-added")
    @Operation(summary = "Get recently added items", description = "Returns top N recently added items in user's wishlist")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recently added items retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<WishlistItemResponse>>> getRecentlyAdded(
            @Parameter(description = "Number of recent items to return", example = "5") @RequestParam(defaultValue = "5") int limit) {
        List<WishlistItemResponse> recent = wishlistService.getRecentlyAdded(limit);
        return ResponseEntity.ok(ApiResponse.success(recent, "Recently added wishlist items retrieved successfully"));
    }

    @PostMapping("/items/{productId}")
    @Operation(summary = "Add product to wishlist", description = "Saves a product to user's wishlist")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product added to wishlist successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Product is inactive or unavailable"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Product already present in wishlist")
    })
    public ResponseEntity<ApiResponse<WishlistResponse>> addProductToWishlist(
            @Parameter(description = "Product ID to add", required = true) @PathVariable Long productId) {
        WishlistResponse updated = wishlistService.addProductToWishlist(productId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(updated, "Product added to wishlist successfully"));
    }

    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Remove product from wishlist", description = "Removes a product from user's wishlist")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product removed from wishlist successfully")
    })
    public ResponseEntity<ApiResponse<WishlistResponse>> removeProductFromWishlist(
            @Parameter(description = "Product ID to remove", required = true) @PathVariable Long productId) {
        WishlistResponse updated = wishlistService.removeProductFromWishlist(productId);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product removed from wishlist successfully"));
    }

    @PostMapping("/items/{productId}/move-to-cart")
    @Operation(summary = "Move product to cart", description = "Transfers a saved item from wishlist to active shopping cart")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product moved to cart successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Product out of stock or inactive")
    })
    public ResponseEntity<ApiResponse<CartResponse>> moveToCart(
            @Parameter(description = "Product ID to move", required = true) @PathVariable Long productId,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        CartResponse cartResponse = wishlistService.moveToCart(productId, sessionId);
        return ResponseEntity.ok(ApiResponse.success(cartResponse, "Product moved to cart successfully"));
    }
}
