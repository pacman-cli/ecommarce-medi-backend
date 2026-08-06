package com.example.ecommerce.order.dto.request;

import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Filter parameters for searching and querying order history.
 */
@Schema(description = "Order search and filter parameters")
public class OrderFilterRequest {

    @Schema(description = "Keyword search matching order number, invoice number or customer name", example = "ORD-2026")
    private String search;

    @Schema(description = "Filter by order status", example = "CONFIRMED")
    private OrderStatus status;

    @Schema(description = "Filter by payment status", example = "PAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Filter orders placed on or after date")
    private Instant startDate;

    @Schema(description = "Filter orders placed on or before date")
    private Instant endDate;

    @Schema(description = "Minimum total amount filter", example = "50.00")
    private BigDecimal minAmount;

    @Schema(description = "Maximum total amount filter", example = "500.00")
    private BigDecimal maxAmount;

    public OrderFilterRequest() {
    }

    public OrderFilterRequest(String search, OrderStatus status, PaymentStatus paymentStatus, Instant startDate, Instant endDate, BigDecimal minAmount, BigDecimal maxAmount) {
        this.search = search;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }

    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }

    public BigDecimal getMinAmount() { return minAmount; }
    public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }

    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }

    public static OrderFilterRequestBuilder builder() { return new OrderFilterRequestBuilder(); }

    public static class OrderFilterRequestBuilder {
        private String search;
        private OrderStatus status;
        private PaymentStatus paymentStatus;
        private Instant startDate;
        private Instant endDate;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;

        OrderFilterRequestBuilder() {}

        public OrderFilterRequestBuilder search(String search) { this.search = search; return this; }
        public OrderFilterRequestBuilder status(OrderStatus status) { this.status = status; return this; }
        public OrderFilterRequestBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public OrderFilterRequestBuilder startDate(Instant startDate) { this.startDate = startDate; return this; }
        public OrderFilterRequestBuilder endDate(Instant endDate) { this.endDate = endDate; return this; }
        public OrderFilterRequestBuilder minAmount(BigDecimal minAmount) { this.minAmount = minAmount; return this; }
        public OrderFilterRequestBuilder maxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; return this; }

        public OrderFilterRequest build() {
            return new OrderFilterRequest(search, status, paymentStatus, startDate, endDate, minAmount, maxAmount);
        }
    }
}
