package com.example.ecommerce.cart.dto.response;

import com.example.ecommerce.cart.entity.CartStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * Full shopping cart projection response DTO with calculated price breakdowns.
 */
@Schema(description = "Cart details response")
public class CartResponse {

    @Schema(description = "Cart ID", example = "100")
    private Long id;

    @Schema(description = "Associated User ID (null for guest cart)", example = "1")
    private Long userId;

    @Schema(description = "Guest Session ID", example = "guest-session-12345")
    private String sessionId;

    @Schema(description = "List of line items")
    private List<CartItemResponse> items;

    @Schema(description = "Applied coupon promo code", example = "SAVE10")
    private String couponCode;

    @Schema(description = "Promotional coupon discount amount", example = "5.00")
    private BigDecimal couponDiscount;

    @Schema(description = "Gross items subtotal before discounts", example = "25.00")
    private BigDecimal subtotal;

    @Schema(description = "Product-level discount savings", example = "2.00")
    private BigDecimal itemDiscount;

    @Schema(description = "Total calculated sales tax", example = "1.15")
    private BigDecimal taxAmount;

    @Schema(description = "Shipping charge", example = "5.00")
    private BigDecimal shippingCharge;

    @Schema(description = "Final grand total amount", example = "24.15")
    private BigDecimal grandTotal;

    @Schema(description = "Total number of items in cart", example = "3")
    private Integer totalItems;

    @Schema(description = "Cart status", example = "ACTIVE")
    private CartStatus status;

    public CartResponse() {
    }

    public CartResponse(Long id, Long userId, String sessionId, List<CartItemResponse> items, String couponCode, BigDecimal couponDiscount, BigDecimal subtotal, BigDecimal itemDiscount, BigDecimal taxAmount, BigDecimal shippingCharge, BigDecimal grandTotal, Integer totalItems, CartStatus status) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
        this.items = items;
        this.couponCode = couponCode;
        this.couponDiscount = couponDiscount;
        this.subtotal = subtotal;
        this.itemDiscount = itemDiscount;
        this.taxAmount = taxAmount;
        this.shippingCharge = shippingCharge;
        this.grandTotal = grandTotal;
        this.totalItems = totalItems;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public List<CartItemResponse> getItems() { return items; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public BigDecimal getCouponDiscount() { return couponDiscount; }
    public void setCouponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getItemDiscount() { return itemDiscount; }
    public void setItemDiscount(BigDecimal itemDiscount) { this.itemDiscount = itemDiscount; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getShippingCharge() { return shippingCharge; }
    public void setShippingCharge(BigDecimal shippingCharge) { this.shippingCharge = shippingCharge; }

    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public CartStatus getStatus() { return status; }
    public void setStatus(CartStatus status) { this.status = status; }

    public static CartResponseBuilder builder() { return new CartResponseBuilder(); }

    public static class CartResponseBuilder {
        private Long id;
        private Long userId;
        private String sessionId;
        private List<CartItemResponse> items;
        private String couponCode;
        private BigDecimal couponDiscount;
        private BigDecimal subtotal;
        private BigDecimal itemDiscount;
        private BigDecimal taxAmount;
        private BigDecimal shippingCharge;
        private BigDecimal grandTotal;
        private Integer totalItems;
        private CartStatus status;

        CartResponseBuilder() {}

        public CartResponseBuilder id(Long id) { this.id = id; return this; }
        public CartResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public CartResponseBuilder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public CartResponseBuilder items(List<CartItemResponse> items) { this.items = items; return this; }
        public CartResponseBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public CartResponseBuilder couponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; return this; }
        public CartResponseBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public CartResponseBuilder itemDiscount(BigDecimal itemDiscount) { this.itemDiscount = itemDiscount; return this; }
        public CartResponseBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public CartResponseBuilder shippingCharge(BigDecimal shippingCharge) { this.shippingCharge = shippingCharge; return this; }
        public CartResponseBuilder grandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; return this; }
        public CartResponseBuilder totalItems(Integer totalItems) { this.totalItems = totalItems; return this; }
        public CartResponseBuilder status(CartStatus status) { this.status = status; return this; }

        public CartResponse build() {
            return new CartResponse(id, userId, sessionId, items, couponCode, couponDiscount, subtotal, itemDiscount, taxAmount, shippingCharge, grandTotal, totalItems, status);
        }
    }
}
