package com.example.ecommerce.wishlist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Line item response projection for wishlist.
 */
@Schema(description = "Wishlist item details response")
public class WishlistItemResponse {

    @Schema(description = "Wishlist item ID", example = "10")
    private Long id;

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Product name", example = "Paracetamol 500mg Tablets")
    private String productName;

    @Schema(description = "Product SKU", example = "MED-PARA-500")
    private String productSku;

    @Schema(description = "Product slug", example = "paracetamol-500mg-tablets")
    private String productSlug;

    @Schema(description = "Thumbnail image URL", example = "https://images.example.com/products/thumb-para.jpg")
    private String thumbnail;

    @Schema(description = "Current selling price", example = "5.99")
    private BigDecimal sellingPrice;

    @Schema(description = "Current discount price", example = "4.99")
    private BigDecimal discountPrice;

    @Schema(description = "Stock availability flag", example = "true")
    private boolean inStock;

    @Schema(description = "Timestamp when added to wishlist")
    private Instant addedAt;

    public WishlistItemResponse() {
    }

    public WishlistItemResponse(Long id, Long productId, String productName, String productSku, String productSlug, String thumbnail, BigDecimal sellingPrice, BigDecimal discountPrice, boolean inStock, Instant addedAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.productSlug = productSlug;
        this.thumbnail = thumbnail;
        this.sellingPrice = sellingPrice;
        this.discountPrice = discountPrice;
        this.inStock = inStock;
        this.addedAt = addedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public String getProductSlug() { return productSlug; }
    public void setProductSlug(String productSlug) { this.productSlug = productSlug; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public BigDecimal getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; }

    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }

    public Instant getAddedAt() { return addedAt; }
    public void setAddedAt(Instant addedAt) { this.addedAt = addedAt; }

    public static WishlistItemResponseBuilder builder() { return new WishlistItemResponseBuilder(); }

    public static class WishlistItemResponseBuilder {
        private Long id;
        private Long productId;
        private String productName;
        private String productSku;
        private String productSlug;
        private String thumbnail;
        private BigDecimal sellingPrice;
        private BigDecimal discountPrice;
        private boolean inStock;
        private Instant addedAt;

        WishlistItemResponseBuilder() {}

        public WishlistItemResponseBuilder id(Long id) { this.id = id; return this; }
        public WishlistItemResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public WishlistItemResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public WishlistItemResponseBuilder productSku(String productSku) { this.productSku = productSku; return this; }
        public WishlistItemResponseBuilder productSlug(String productSlug) { this.productSlug = productSlug; return this; }
        public WishlistItemResponseBuilder thumbnail(String thumbnail) { this.thumbnail = thumbnail; return this; }
        public WishlistItemResponseBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public WishlistItemResponseBuilder discountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; return this; }
        public WishlistItemResponseBuilder inStock(boolean inStock) { this.inStock = inStock; return this; }
        public WishlistItemResponseBuilder addedAt(Instant addedAt) { this.addedAt = addedAt; return this; }

        public WishlistItemResponse build() {
            return new WishlistItemResponse(id, productId, productName, productSku, productSlug, thumbnail, sellingPrice, discountPrice, inStock, addedAt);
        }
    }
}
