package com.example.ecommerce.cart.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Shopping cart entity supporting guest session carts, authenticated user carts,
 * coupon discounts, tax calculation, shipping charges and total price breakdowns.
 */
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "coupon_code", length = 50)
    private String couponCode;

    @Column(name = "coupon_discount", precision = 19, scale = 2)
    private BigDecimal couponDiscount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "item_discount", nullable = false, precision = 19, scale = 2)
    private BigDecimal itemDiscount = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "shipping_charge", nullable = false, precision = 19, scale = 2)
    private BigDecimal shippingCharge = BigDecimal.ZERO;

    @Column(name = "grand_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CartStatus status = CartStatus.ACTIVE;

    public Cart() {
    }

    public void addItem(CartItem item) {
        if (item != null) {
            if (this.items == null) {
                this.items = new ArrayList<>();
            }
            item.setCart(this);
            item.recalculatePrices();
            this.items.add(item);
            recalculateTotals();
        }
    }

    public void removeItem(CartItem item) {
        if (item != null && this.items != null) {
            this.items.remove(item);
            item.setCart(null);
            recalculateTotals();
        }
    }

    public void clearItems() {
        if (this.items != null) {
            this.items.clear();
        }
        recalculateTotals();
    }

    public void recalculateTotals() {
        BigDecimal sub = BigDecimal.ZERO;
        BigDecimal itemDisc = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;

        if (this.items != null && !this.items.isEmpty()) {
            for (CartItem item : this.items) {
                item.recalculatePrices();
                BigDecimal sellingPrice = item.getUnitPrice();
                sub = sub.add(sellingPrice.multiply(BigDecimal.valueOf(item.getQuantity())));

                if (item.getDiscountPrice() != null && item.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal itemSavings = sellingPrice.subtract(item.getDiscountPrice());
                    itemDisc = itemDisc.add(itemSavings.multiply(BigDecimal.valueOf(item.getQuantity())));
                }

                if (item.getTaxAmount() != null) {
                    tax = tax.add(item.getTaxAmount());
                }
            }
        }

        this.subtotal = sub;
        this.itemDiscount = itemDisc;
        this.taxAmount = tax;

        // Apply coupon discount if any
        BigDecimal effectiveCouponDisc = this.couponDiscount != null ? this.couponDiscount : BigDecimal.ZERO;
        BigDecimal netSubtotal = sub.subtract(itemDisc).subtract(effectiveCouponDisc);
        if (netSubtotal.compareTo(BigDecimal.ZERO) < 0) {
            netSubtotal = BigDecimal.ZERO;
        }

        // Standard shipping charge rule (e.g. Free shipping over $50, else $5.00 if cart has items)
        if (this.items != null && !this.items.isEmpty()) {
            if (netSubtotal.compareTo(new BigDecimal("50.00")) >= 0) {
                this.shippingCharge = BigDecimal.ZERO;
            } else {
                this.shippingCharge = new BigDecimal("5.00");
            }
        } else {
            this.shippingCharge = BigDecimal.ZERO;
        }

        this.grandTotal = netSubtotal.add(tax).add(this.shippingCharge);
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

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

    public CartStatus getStatus() { return status; }
    public void setStatus(CartStatus status) { this.status = status; }

    public static CartBuilder builder() { return new CartBuilder(); }

    public static class CartBuilder {
        private User user;
        private String sessionId;
        private List<CartItem> items = new ArrayList<>();
        private String couponCode;
        private BigDecimal couponDiscount = BigDecimal.ZERO;
        private BigDecimal subtotal = BigDecimal.ZERO;
        private BigDecimal itemDiscount = BigDecimal.ZERO;
        private BigDecimal taxAmount = BigDecimal.ZERO;
        private BigDecimal shippingCharge = BigDecimal.ZERO;
        private BigDecimal grandTotal = BigDecimal.ZERO;
        private CartStatus status = CartStatus.ACTIVE;

        CartBuilder() {}

        public CartBuilder user(User user) { this.user = user; return this; }
        public CartBuilder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public CartBuilder items(List<CartItem> items) { this.items = items; return this; }
        public CartBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public CartBuilder couponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; return this; }
        public CartBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public CartBuilder itemDiscount(BigDecimal itemDiscount) { this.itemDiscount = itemDiscount; return this; }
        public CartBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public CartBuilder shippingCharge(BigDecimal shippingCharge) { this.shippingCharge = shippingCharge; return this; }
        public CartBuilder grandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; return this; }
        public CartBuilder status(CartStatus status) { this.status = status; return this; }

        public Cart build() {
            Cart c = new Cart();
            c.setUser(user);
            c.setSessionId(sessionId);
            c.setItems(items != null ? items : new ArrayList<>());
            c.setCouponCode(couponCode);
            c.setCouponDiscount(couponDiscount != null ? couponDiscount : BigDecimal.ZERO);
            c.setSubtotal(subtotal != null ? subtotal : BigDecimal.ZERO);
            c.setItemDiscount(itemDiscount != null ? itemDiscount : BigDecimal.ZERO);
            c.setTaxAmount(taxAmount != null ? taxAmount : BigDecimal.ZERO);
            c.setShippingCharge(shippingCharge != null ? shippingCharge : BigDecimal.ZERO);
            c.setGrandTotal(grandTotal != null ? grandTotal : BigDecimal.ZERO);
            c.setStatus(status != null ? status : CartStatus.ACTIVE);
            c.recalculateTotals();
            return c;
        }
    }
}
