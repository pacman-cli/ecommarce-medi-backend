package com.example.ecommerce.cart.entity;

import com.example.ecommerce.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Line item entry inside a user or guest shopping cart.
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "discount_price", precision = 19, scale = 2)
    private BigDecimal discountPrice;

    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public CartItem() {
    }

    public CartItem(Long id, Cart cart, Product product, Integer quantity, BigDecimal unitPrice, BigDecimal discountPrice, BigDecimal taxAmount, BigDecimal totalPrice) {
        this.id = id;
        this.cart = cart;
        this.product = product;
        this.quantity = quantity != null ? quantity : 1;
        this.unitPrice = unitPrice != null ? unitPrice : BigDecimal.ZERO;
        this.discountPrice = discountPrice;
        this.taxAmount = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        this.totalPrice = totalPrice != null ? totalPrice : BigDecimal.ZERO;
    }

    public void recalculatePrices() {
        if (product != null) {
            this.unitPrice = product.getSellingPrice() != null ? product.getSellingPrice() : BigDecimal.ZERO;
            this.discountPrice = product.getDiscountPrice();
            BigDecimal effectivePrice = (discountPrice != null && discountPrice.compareTo(BigDecimal.ZERO) > 0)
                    ? discountPrice : unitPrice;

            BigDecimal baseTotal = effectivePrice.multiply(BigDecimal.valueOf(quantity != null ? quantity : 1));

            // Tax calculation
            if (product.getTax() != null && product.getTax().compareTo(BigDecimal.ZERO) > 0) {
                this.taxAmount = baseTotal.multiply(product.getTax()).divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
            } else {
                this.taxAmount = BigDecimal.ZERO;
            }

            this.totalPrice = baseTotal.add(this.taxAmount);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

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

    public static CartItemBuilder builder() { return new CartItemBuilder(); }

    public static class CartItemBuilder {
        private Long id;
        private Cart cart;
        private Product product;
        private Integer quantity = 1;
        private BigDecimal unitPrice = BigDecimal.ZERO;
        private BigDecimal discountPrice;
        private BigDecimal taxAmount = BigDecimal.ZERO;
        private BigDecimal totalPrice = BigDecimal.ZERO;

        CartItemBuilder() {}

        public CartItemBuilder id(Long id) { this.id = id; return this; }
        public CartItemBuilder cart(Cart cart) { this.cart = cart; return this; }
        public CartItemBuilder product(Product product) { this.product = product; return this; }
        public CartItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public CartItemBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public CartItemBuilder discountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; return this; }
        public CartItemBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public CartItemBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }

        public CartItem build() {
            CartItem item = new CartItem(id, cart, product, quantity, unitPrice, discountPrice, taxAmount, totalPrice);
            item.recalculatePrices();
            return item;
        }
    }
}
