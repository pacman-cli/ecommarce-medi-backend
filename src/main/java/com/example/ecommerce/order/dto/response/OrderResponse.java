package com.example.ecommerce.order.dto.response;

import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Master order response DTO.
 */
@Schema(description = "Order details response")
public class OrderResponse {

    @Schema(description = "Order ID", example = "100")
    private Long id;

    @Schema(description = "Unique order tracking number code", example = "ORD-20260804-98421")
    private String orderNumber;

    @Schema(description = "Generated tax invoice number", example = "INV-20260804-98421")
    private String invoiceNumber;

    @Schema(description = "User ID (null for guest checkout)", example = "1")
    private Long userId;

    @Schema(description = "Customer email", example = "john.doe@example.com")
    private String customerEmail;

    @Schema(description = "Order line items")
    private List<OrderItemResponse> items;

    @Schema(description = "Order lifecycle timeline")
    private List<OrderTimelineResponse> timelines;

    @Schema(description = "Order status", example = "CONFIRMED")
    private OrderStatus status;

    @Schema(description = "Payment processing status", example = "PAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Shipping address")
    private OrderAddressResponse shippingAddress;

    @Schema(description = "Billing address")
    private OrderAddressResponse billingAddress;

    @Schema(description = "Subtotal before discounts", example = "100.00")
    private BigDecimal subtotal;

    @Schema(description = "Product level discount savings", example = "10.00")
    private BigDecimal itemDiscount;

    @Schema(description = "Coupon code applied", example = "SAVE10")
    private String couponCode;

    @Schema(description = "Coupon discount savings", example = "5.00")
    private BigDecimal couponDiscount;

    @Schema(description = "Shipping fee", example = "5.00")
    private BigDecimal shippingFee;

    @Schema(description = "Sales tax", example = "4.50")
    private BigDecimal taxAmount;

    @Schema(description = "Grand total", example = "94.50")
    private BigDecimal grandTotal;

    @Schema(description = "Carrier tracking number", example = "TRACK-FDX-998811")
    private String trackingNumber;

    @Schema(description = "Customer delivery notes", example = "Leave at front desk")
    private String orderNotes;

    @Schema(description = "Cancellation timestamp (if cancelled)")
    private Instant cancelledAt;

    @Schema(description = "Cancellation reason")
    private String cancelReason;

    @Schema(description = "Delivery timestamp (if delivered)")
    private Instant deliveredAt;

    @Schema(description = "Order placement timestamp")
    private Instant createdAt;

    public OrderResponse() {
    }

    public OrderResponse(Long id, String orderNumber, String invoiceNumber, Long userId, String customerEmail, List<OrderItemResponse> items, List<OrderTimelineResponse> timelines, OrderStatus status, PaymentStatus paymentStatus, OrderAddressResponse shippingAddress, OrderAddressResponse billingAddress, BigDecimal subtotal, BigDecimal itemDiscount, String couponCode, BigDecimal couponDiscount, BigDecimal shippingFee, BigDecimal taxAmount, BigDecimal grandTotal, String trackingNumber, String orderNotes, Instant cancelledAt, String cancelReason, Instant deliveredAt, Instant createdAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.invoiceNumber = invoiceNumber;
        this.userId = userId;
        this.customerEmail = customerEmail;
        this.items = items;
        this.timelines = timelines;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.subtotal = subtotal;
        this.itemDiscount = itemDiscount;
        this.couponCode = couponCode;
        this.couponDiscount = couponDiscount;
        this.shippingFee = shippingFee;
        this.taxAmount = taxAmount;
        this.grandTotal = grandTotal;
        this.trackingNumber = trackingNumber;
        this.orderNotes = orderNotes;
        this.cancelledAt = cancelledAt;
        this.cancelReason = cancelReason;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }

    public List<OrderTimelineResponse> getTimelines() { return timelines; }
    public void setTimelines(List<OrderTimelineResponse> timelines) { this.timelines = timelines; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public OrderAddressResponse getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(OrderAddressResponse shippingAddress) { this.shippingAddress = shippingAddress; }

    public OrderAddressResponse getBillingAddress() { return billingAddress; }
    public void setBillingAddress(OrderAddressResponse billingAddress) { this.billingAddress = billingAddress; }

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

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static OrderResponseBuilder builder() { return new OrderResponseBuilder(); }

    public static class OrderResponseBuilder {
        private Long id;
        private String orderNumber;
        private String invoiceNumber;
        private Long userId;
        private String customerEmail;
        private List<OrderItemResponse> items;
        private List<OrderTimelineResponse> timelines;
        private OrderStatus status;
        private PaymentStatus paymentStatus;
        private OrderAddressResponse shippingAddress;
        private OrderAddressResponse billingAddress;
        private BigDecimal subtotal;
        private BigDecimal itemDiscount;
        private String couponCode;
        private BigDecimal couponDiscount;
        private BigDecimal shippingFee;
        private BigDecimal taxAmount;
        private BigDecimal grandTotal;
        private String trackingNumber;
        private String orderNotes;
        private Instant cancelledAt;
        private String cancelReason;
        private Instant deliveredAt;
        private Instant createdAt;

        OrderResponseBuilder() {}

        public OrderResponseBuilder id(Long id) { this.id = id; return this; }
        public OrderResponseBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public OrderResponseBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public OrderResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public OrderResponseBuilder customerEmail(String customerEmail) { this.customerEmail = customerEmail; return this; }
        public OrderResponseBuilder items(List<OrderItemResponse> items) { this.items = items; return this; }
        public OrderResponseBuilder timelines(List<OrderTimelineResponse> timelines) { this.timelines = timelines; return this; }
        public OrderResponseBuilder status(OrderStatus status) { this.status = status; return this; }
        public OrderResponseBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public OrderResponseBuilder shippingAddress(OrderAddressResponse shippingAddress) { this.shippingAddress = shippingAddress; return this; }
        public OrderResponseBuilder billingAddress(OrderAddressResponse billingAddress) { this.billingAddress = billingAddress; return this; }
        public OrderResponseBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public OrderResponseBuilder itemDiscount(BigDecimal itemDiscount) { this.itemDiscount = itemDiscount; return this; }
        public OrderResponseBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public OrderResponseBuilder couponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; return this; }
        public OrderResponseBuilder shippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; return this; }
        public OrderResponseBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public OrderResponseBuilder grandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; return this; }
        public OrderResponseBuilder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public OrderResponseBuilder orderNotes(String orderNotes) { this.orderNotes = orderNotes; return this; }
        public OrderResponseBuilder cancelledAt(Instant cancelledAt) { this.cancelledAt = cancelledAt; return this; }
        public OrderResponseBuilder cancelReason(String cancelReason) { this.cancelReason = cancelReason; return this; }
        public OrderResponseBuilder deliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; return this; }
        public OrderResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public OrderResponse build() {
            return new OrderResponse(id, orderNumber, invoiceNumber, userId, customerEmail, items, timelines, status, paymentStatus, shippingAddress, billingAddress, subtotal, itemDiscount, couponCode, couponDiscount, shippingFee, taxAmount, grandTotal, trackingNumber, orderNotes, cancelledAt, cancelReason, deliveredAt, createdAt);
        }
    }
}
