package com.example.ecommerce.order.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.user.entity.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise order aggregate root managing order placement, line items, embedded shipping/billing addresses,
 * payment statuses, lifecycle state timelines, tracking numbers, invoices and soft deletes.
 */
@Entity
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_orders_order_number", columnNames = "order_number"),
                @UniqueConstraint(name = "uk_orders_invoice_number", columnNames = "invoice_number")
        }
)
@SQLDelete(sql = "UPDATE orders SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Order extends BaseEntity {

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("changedAt ASC")
    private List<OrderTimeline> timelines = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "recipientName", column = @Column(name = "shipping_recipient_name", nullable = false)),
            @AttributeOverride(name = "phone", column = @Column(name = "shipping_phone", nullable = false)),
            @AttributeOverride(name = "streetAddress", column = @Column(name = "shipping_street_address", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "shipping_city", nullable = false)),
            @AttributeOverride(name = "state", column = @Column(name = "shipping_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zip_code", nullable = false)),
            @AttributeOverride(name = "country", column = @Column(name = "shipping_country", nullable = false))
    })
    private OrderAddress shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "recipientName", column = @Column(name = "billing_recipient_name", nullable = false)),
            @AttributeOverride(name = "phone", column = @Column(name = "billing_phone", nullable = false)),
            @AttributeOverride(name = "streetAddress", column = @Column(name = "billing_street_address", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "billing_city", nullable = false)),
            @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "billing_zip_code", nullable = false)),
            @AttributeOverride(name = "country", column = @Column(name = "billing_country", nullable = false))
    })
    private OrderAddress billingAddress;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "item_discount", nullable = false, precision = 19, scale = 2)
    private BigDecimal itemDiscount = BigDecimal.ZERO;

    @Column(name = "coupon_code", length = 50)
    private String couponCode;

    @Column(name = "coupon_discount", nullable = false, precision = 19, scale = 2)
    private BigDecimal couponDiscount = BigDecimal.ZERO;

    @Column(name = "shipping_fee", nullable = false, precision = 19, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "grand_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "order_notes", length = 500)
    private String orderNotes;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "cancel_reason", length = 250)
    private String cancelReason;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Order() {
    }

    public void addItem(OrderItem item) {
        if (item != null) {
            if (this.items == null) {
                this.items = new ArrayList<>();
            }
            item.setOrder(this);
            this.items.add(item);
        }
    }

    public void addTimeline(OrderStatus status, String note, String changedBy) {
        if (this.timelines == null) {
            this.timelines = new ArrayList<>();
        }
        OrderTimeline timeline = OrderTimeline.builder()
                .order(this)
                .status(status)
                .note(note)
                .changedBy(changedBy)
                .changedAt(Instant.now())
                .build();
        this.timelines.add(timeline);
    }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public List<OrderTimeline> getTimelines() { return timelines; }
    public void setTimelines(List<OrderTimeline> timelines) { this.timelines = timelines; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public OrderAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(OrderAddress shippingAddress) { this.shippingAddress = shippingAddress; }

    public OrderAddress getBillingAddress() { return billingAddress; }
    public void setBillingAddress(OrderAddress billingAddress) { this.billingAddress = billingAddress; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getItemDiscount() { return itemDiscount; }
    public void setItemDiscount(BigDecimal itemDiscount) { this.itemDiscount = itemDiscount; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public BigDecimal getCouponDiscount() { return couponDiscount; }
    public void setCouponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; }

    public BigDecimal getShippingFee() { return shippingFee; }
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getOrderNotes() { return orderNotes; }
    public void setOrderNotes(String orderNotes) { this.orderNotes = orderNotes; }

    public Instant getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(Instant cancelledAt) { this.cancelledAt = cancelledAt; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public Instant getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static OrderBuilder builder() { return new OrderBuilder(); }

    public static class OrderBuilder {
        private String orderNumber;
        private String invoiceNumber;
        private User user;
        private List<OrderItem> items = new ArrayList<>();
        private List<OrderTimeline> timelines = new ArrayList<>();
        private OrderStatus status = OrderStatus.PENDING;
        private PaymentStatus paymentStatus = PaymentStatus.PENDING;
        private OrderAddress shippingAddress;
        private OrderAddress billingAddress;
        private BigDecimal subtotal = BigDecimal.ZERO;
        private BigDecimal itemDiscount = BigDecimal.ZERO;
        private String couponCode;
        private BigDecimal couponDiscount = BigDecimal.ZERO;
        private BigDecimal shippingFee = BigDecimal.ZERO;
        private BigDecimal taxAmount = BigDecimal.ZERO;
        private BigDecimal grandTotal = BigDecimal.ZERO;
        private String trackingNumber;
        private String orderNotes;
        private Instant cancelledAt;
        private String cancelReason;
        private Instant deliveredAt;

        OrderBuilder() {}

        public OrderBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public OrderBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public OrderBuilder user(User user) { this.user = user; return this; }
        public OrderBuilder items(List<OrderItem> items) { this.items = items; return this; }
        public OrderBuilder timelines(List<OrderTimeline> timelines) { this.timelines = timelines; return this; }
        public OrderBuilder status(OrderStatus status) { this.status = status; return this; }
        public OrderBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public OrderBuilder shippingAddress(OrderAddress shippingAddress) { this.shippingAddress = shippingAddress; return this; }
        public OrderBuilder billingAddress(OrderAddress billingAddress) { this.billingAddress = billingAddress; return this; }
        public OrderBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public OrderBuilder itemDiscount(BigDecimal itemDiscount) { this.itemDiscount = itemDiscount; return this; }
        public OrderBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public OrderBuilder couponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; return this; }
        public OrderBuilder shippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; return this; }
        public OrderBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public OrderBuilder grandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; return this; }
        public OrderBuilder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public OrderBuilder orderNotes(String orderNotes) { this.orderNotes = orderNotes; return this; }
        public OrderBuilder cancelledAt(Instant cancelledAt) { this.cancelledAt = cancelledAt; return this; }
        public OrderBuilder cancelReason(String cancelReason) { this.cancelReason = cancelReason; return this; }
        public OrderBuilder deliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; return this; }

        public Order build() {
            Order o = new Order();
            o.setOrderNumber(orderNumber);
            o.setInvoiceNumber(invoiceNumber);
            o.setUser(user);
            o.setItems(items != null ? items : new ArrayList<>());
            o.setTimelines(timelines != null ? timelines : new ArrayList<>());
            o.setStatus(status != null ? status : OrderStatus.PENDING);
            o.setPaymentStatus(paymentStatus != null ? paymentStatus : PaymentStatus.PENDING);
            o.setShippingAddress(shippingAddress);
            o.setBillingAddress(billingAddress);
            o.setSubtotal(subtotal != null ? subtotal : BigDecimal.ZERO);
            o.setItemDiscount(itemDiscount != null ? itemDiscount : BigDecimal.ZERO);
            o.setCouponCode(couponCode);
            o.setCouponDiscount(couponDiscount != null ? couponDiscount : BigDecimal.ZERO);
            o.setShippingFee(shippingFee != null ? shippingFee : BigDecimal.ZERO);
            o.setTaxAmount(taxAmount != null ? taxAmount : BigDecimal.ZERO);
            o.setGrandTotal(grandTotal != null ? grandTotal : BigDecimal.ZERO);
            o.setTrackingNumber(trackingNumber);
            o.setOrderNotes(orderNotes);
            o.setCancelledAt(cancelledAt);
            o.setCancelReason(cancelReason);
            o.setDeliveredAt(deliveredAt);
            return o;
        }
    }
}
