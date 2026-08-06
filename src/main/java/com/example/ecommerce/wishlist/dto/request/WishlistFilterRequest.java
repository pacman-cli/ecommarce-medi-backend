package com.example.ecommerce.wishlist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Filter criteria for paginated wishlist items.
 */
@Schema(description = "Wishlist search and filter parameters")
public class WishlistFilterRequest {

    @Schema(description = "Keyword search matching product name or SKU", example = "Paracetamol")
    private String search;

    @Schema(description = "Filter by Category ID", example = "12")
    private Long categoryId;

    @Schema(description = "Filter by Brand ID", example = "5")
    private Long brandId;

    public WishlistFilterRequest() {
    }

    public WishlistFilterRequest(String search, Long categoryId, Long brandId) {
        this.search = search;
        this.categoryId = categoryId;
        this.brandId = brandId;
    }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public static WishlistFilterRequestBuilder builder() { return new WishlistFilterRequestBuilder(); }

    public static class WishlistFilterRequestBuilder {
        private String search;
        private Long categoryId;
        private Long brandId;

        WishlistFilterRequestBuilder() {}

        public WishlistFilterRequestBuilder search(String search) { this.search = search; return this; }
        public WishlistFilterRequestBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public WishlistFilterRequestBuilder brandId(Long brandId) { this.brandId = brandId; return this; }

        public WishlistFilterRequest build() {
            return new WishlistFilterRequest(search, categoryId, brandId);
        }
    }
}
