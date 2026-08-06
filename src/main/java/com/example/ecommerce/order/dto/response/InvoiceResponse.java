package com.example.ecommerce.order.dto.response;

import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Invoice representation DTO generated for a placed order.
 */
@Schema(description = "Invoice details response")
public class InvoiceResponse {

    @Schema(description = "Invoice number", example = "INV-20260804-98421")
    private String invoiceNumber;

    @Schema(description = "Order number", example = "ORD-20260804-98421")
    private String orderNumber;

    @Schema(description = "Invoice issue date")
    private Instant issueDate;

    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;

    @Schema(description = "Customer email", example = "user@example.com")
    private String customerEmail;

    @Schema(description = "Shipping address")
    private OrderAddressResponse shippingAddress;

    @Schema(description = "Billing address")
    private OrderAddressResponse billingAddress;

    @Schema(description = "Line items")
    private List<OrderItemResponse> items;

    @Schema(description = "Gross subtotal", example = "100.00")
    private BigDecimal subtotal;

    @Schema(description = "Item discount savings", example = "10.00")
    private BigDecimal itemDiscount;

    @Schema(description = "Coupon code applied", example = "SAVE10")
    private String couponCode;

    @Schema(description = "Coupon discount amount", example = "5.00")
    private BigDecimal couponDiscount;

    @Schema(description = "Shipping charge", example = "5.00")
    private BigDecimal shippingFee;

    @Schema(description = "Total sales tax", example = "4.50")
    private BigDecimal taxAmount;

    @Schema(description = "Grand total amount due/paid", example = "94.50")
    private BigDecimal grandTotal;

    @Schema(description = "Payment status", example = "PAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Order status", example = "CONFIRMED")
    private OrderStatus orderStatus;

    public InvoiceResponse() {
    }

    public InvoiceResponse(String invoiceNumber, String orderNumber, Instant issueDate, String customerName, String customerEmail, OrderAddressResponse shippingAddress, OrderAddressResponse billingAddress, List<OrderItemResponse> items, BigDecimal subtotal, BigDecimal itemDiscount, String couponCode, BigDecimal couponDiscount, BigDecimal shippingFee, BigDecimal taxAmount, BigDecimal grandTotal, PaymentStatus paymentStatus, OrderStatus orderStatus) {
        this.invoiceNumber = invoiceNumber;
        this.orderNumber = orderNumber;
        this.issueDate = issueDate;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.items = items;
        this.subtotal = subtotal;
        this.itemDiscount = itemDiscount;
        this.couponCode = couponCode;
        this.couponDiscount = couponDiscount;
        this.shippingFee = shippingFee;
        this.taxAmount = taxAmount;
        this.grandTotal = grandTotal;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Instant getIssueDate() { return issueDate; }
    public void setIssueDate(Instant issueDate) { this.issueDate = issueDate; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public OrderAddressResponse getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(OrderAddressResponse shippingAddress) { this.shippingAddress = shippingAddress; }

    public OrderAddressResponse getBillingAddress() { return billingAddress; }
    public void setBillingAddress(OrderAddressResponse billingAddress) { this.billingAddress = billingAddress; }

    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }

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

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

    public static InvoiceResponseBuilder builder() { return new InvoiceResponseBuilder(); }

    public static class InvoiceResponseBuilder {
        private String invoiceNumber;
        private String orderNumber;
        private Instant issueDate;
        private String customerName;
        private String customerEmail;
        private OrderAddressResponse shippingAddress;
        private OrderAddressResponse billingAddress;
        private List<OrderItemResponse> items;
        private BigDecimal subtotal;
        private BigDecimal itemDiscount;
        private String couponCode;
        private BigDecimal couponDiscount;
        private BigDecimal shippingFee;
        private BigDecimal taxAmount;
        private BigDecimal grandTotal;
        private PaymentStatus paymentStatus;
        private OrderStatus orderStatus;

        InvoiceResponseBuilder() {}

        public InvoiceResponseBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public InvoiceResponseBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public InvoiceResponseBuilder issueDate(Instant issueDate) { this.issueDate = issueDate; return this; }
        public InvoiceResponseBuilder customerName(String customerName) { this.customerName = customerName; return this; }
        public InvoiceResponseBuilder customerEmail(String customerEmail) { this.customerEmail = customerEmail; return this; }
        public InvoiceResponseBuilder shippingAddress(OrderAddressResponse shippingAddress) { this.shippingAddress = shippingAddress; return this; }
        public InvoiceResponseBuilder billingAddress(OrderAddressResponse billingAddress) { this.billingAddress = billingAddress; return this; }
        public InvoiceResponseBuilder items(List<OrderItemResponse> items) { this.items = items; return this; }
        public InvoiceResponseBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public InvoiceResponseBuilder itemDiscount(BigDecimal itemDiscount) { this.itemDiscount = itemDiscount; return this; }
        public InvoiceResponseBuilder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public InvoiceResponseBuilder couponDiscount(BigDecimal couponDiscount) { this.couponDiscount = couponDiscount; return this; }
        public InvoiceResponseBuilder shippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; return this; }
        public InvoiceResponseBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public InvoiceResponseBuilder grandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; return this; }
        public InvoiceResponseBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public InvoiceResponseBuilder orderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; return this; }

        public InvoiceResponse build() {
            return new InvoiceResponse(invoiceNumber, orderNumber, issueDate, customerName, customerEmail, shippingAddress, billingAddress, items, subtotal, itemDiscount, couponCode, couponDiscount, shippingFee, taxAmount, grandTotal, paymentStatus, orderStatus);
        }
    }
}
