package com.example.ecommerce.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Line item response projection for shopping cart.
 */
@Schema(description = "Cart item response")
public class CartItemResponse {

    @Schema(description = "Cart item ID", example = "10")
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

    @Schema(description = "Selected quantity", example = "2")
    private Integer quantity;

    @Schema(description = "Regular unit selling price", example = "5.99")
    private BigDecimal unitPrice;

    @Schema(description = "Discounted unit price", example = "4.99")
    private BigDecimal discountPrice;

    @Schema(description = "Calculated item tax", example = "0.50")
    private BigDecimal taxAmount;

    @Schema(description = "Line item total price", example = "10.48")
    private BigDecimal totalPrice;

    @Schema(description = "Stock availability status", example = "true")
    private boolean inStock;

    public CartItemResponse() {
    }

    public CartItemResponse(Long id, Long productId, String productName, String productSku, String productSlug, String thumbnail, Integer quantity, BigDecimal unitPrice, BigDecimal discountPrice, BigDecimal taxAmount, BigDecimal totalPrice, boolean inStock) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.productSlug = productSlug;
        this.thumbnail = thumbnail;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPrice = discountPrice;
        this.taxAmount = taxAmount;
        this.totalPrice = totalPrice;
        this.inStock = inStock;
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

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }

    public static CartItemResponseBuilder builder() { return new CartItemResponseBuilder(); }

    public static class CartItemResponseBuilder {
        private Long id;
        private Long productId;
        private String productName;
        private String productSku;
        private String productSlug;
        private String thumbnail;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discountPrice;
        private BigDecimal taxAmount;
        private BigDecimal totalPrice;
        private boolean inStock;

        CartItemResponseBuilder() {}

        public CartItemResponseBuilder id(Long id) { this.id = id; return this; }
        public CartItemResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public CartItemResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public CartItemResponseBuilder productSku(String productSku) { this.productSku = productSku; return this; }
        public CartItemResponseBuilder productSlug(String productSlug) { this.productSlug = productSlug; return this; }
        public CartItemResponseBuilder thumbnail(String thumbnail) { this.thumbnail = thumbnail; return this; }
        public CartItemResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public CartItemResponseBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public CartItemResponseBuilder discountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; return this; }
        public CartItemResponseBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public CartItemResponseBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }
        public CartItemResponseBuilder inStock(boolean inStock) { this.inStock = inStock; return this; }

        public CartItemResponse build() {
            return new CartItemResponse(id, productId, productName, productSku, productSlug, thumbnail, quantity, unitPrice, discountPrice, taxAmount, totalPrice, inStock);
        }
    }
}
