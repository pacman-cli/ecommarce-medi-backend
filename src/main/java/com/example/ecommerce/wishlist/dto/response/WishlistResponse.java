package com.example.ecommerce.wishlist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Full wishlist projection response DTO.
 */
@Schema(description = "Wishlist response")
public class WishlistResponse {

    @Schema(description = "Wishlist ID", example = "50")
    private Long id;

    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Wishlist line items")
    private List<WishlistItemResponse> items;

    @Schema(description = "Total saved items count", example = "4")
    private Integer totalItems;

    public WishlistResponse() {
    }

    public WishlistResponse(Long id, Long userId, List<WishlistItemResponse> items, Integer totalItems) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalItems = totalItems;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<WishlistItemResponse> getItems() { return items; }
    public void setItems(List<WishlistItemResponse> items) { this.items = items; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public static WishlistResponseBuilder builder() { return new WishlistResponseBuilder(); }

    public static class WishlistResponseBuilder {
        private Long id;
        private Long userId;
        private List<WishlistItemResponse> items;
        private Integer totalItems;

        WishlistResponseBuilder() {}

        public WishlistResponseBuilder id(Long id) { this.id = id; return this; }
        public WishlistResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public WishlistResponseBuilder items(List<WishlistItemResponse> items) { this.items = items; return this; }
        public WishlistResponseBuilder totalItems(Integer totalItems) { this.totalItems = totalItems; return this; }

        public WishlistResponse build() {
            return new WishlistResponse(id, userId, items, totalItems);
        }
    }
}
