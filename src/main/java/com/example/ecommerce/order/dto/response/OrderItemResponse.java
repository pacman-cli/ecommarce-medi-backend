package com.example.ecommerce.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Line item snapshot projection response DTO for an order.
 */
@Schema(description = "Order line item details response")
public class OrderItemResponse {

    @Schema(description = "Order item ID", example = "10")
    private Long id;

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Product name at purchase time", example = "Paracetamol 500mg Tablets")
    private String productName;

    @Schema(description = "Product SKU", example = "MED-PARA-500")
    private String productSku;

    @Schema(description = "Purchased quantity", example = "2")
    private Integer quantity;

    @Schema(description = "Unit selling price", example = "5.99")
    private BigDecimal unitPrice;

    @Schema(description = "Unit discount price", example = "4.99")
    private BigDecimal discountPrice;

    @Schema(description = "Item sales tax", example = "0.50")
    private BigDecimal taxAmount;

    @Schema(description = "Line item total price", example = "10.48")
    private BigDecimal totalPrice;

    public OrderItemResponse() {
    }

    public OrderItemResponse(Long id, Long productId, String productName, String productSku, Integer quantity, BigDecimal unitPrice, BigDecimal discountPrice, BigDecimal taxAmount, BigDecimal totalPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPrice = discountPrice;
        this.taxAmount = taxAmount;
        this.totalPrice = totalPrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

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

    public static OrderItemResponseBuilder builder() { return new OrderItemResponseBuilder(); }

    public static class OrderItemResponseBuilder {
        private Long id;
        private Long productId;
        private String productName;
        private String productSku;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discountPrice;
        private BigDecimal taxAmount;
        private BigDecimal totalPrice;

        OrderItemResponseBuilder() {}

        public OrderItemResponseBuilder id(Long id) { this.id = id; return this; }
        public OrderItemResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public OrderItemResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public OrderItemResponseBuilder productSku(String productSku) { this.productSku = productSku; return this; }
        public OrderItemResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public OrderItemResponseBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public OrderItemResponseBuilder discountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; return this; }
        public OrderItemResponseBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public OrderItemResponseBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }

        public OrderItemResponse build() {
            return new OrderItemResponse(id, productId, productName, productSku, quantity, unitPrice, discountPrice, taxAmount, totalPrice);
        }
    }
}
