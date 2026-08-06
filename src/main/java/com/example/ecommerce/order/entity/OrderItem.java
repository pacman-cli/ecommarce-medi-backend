package com.example.ecommerce.order.entity;

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
 * Line item snapshot stored inside a placed customer order.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 100)
    private String productSku;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount_price", precision = 19, scale = 2)
    private BigDecimal discountPrice;

    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    public OrderItem() {
    }

    public OrderItem(Long id, Order order, Product product, String productName, String productSku, Integer quantity, BigDecimal unitPrice, BigDecimal discountPrice, BigDecimal taxAmount, BigDecimal totalPrice) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.productName = productName;
        this.productSku = productSku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPrice = discountPrice;
        this.taxAmount = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        this.totalPrice = totalPrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

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

    public static OrderItemBuilder builder() { return new OrderItemBuilder(); }

    public static class OrderItemBuilder {
        private Long id;
        private Order order;
        private Product product;
        private String productName;
        private String productSku;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discountPrice;
        private BigDecimal taxAmount = BigDecimal.ZERO;
        private BigDecimal totalPrice;

        OrderItemBuilder() {}

        public OrderItemBuilder id(Long id) { this.id = id; return this; }
        public OrderItemBuilder order(Order order) { this.order = order; return this; }
        public OrderItemBuilder product(Product product) { this.product = product; return this; }
        public OrderItemBuilder productName(String productName) { this.productName = productName; return this; }
        public OrderItemBuilder productSku(String productSku) { this.productSku = productSku; return this; }
        public OrderItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public OrderItemBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public OrderItemBuilder discountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; return this; }
        public OrderItemBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public OrderItemBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }

        public OrderItem build() {
            return new OrderItem(id, order, product, productName, productSku, quantity, unitPrice, discountPrice, taxAmount, totalPrice);
        }
    }
}
