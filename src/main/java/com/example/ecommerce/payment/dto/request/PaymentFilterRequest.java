package com.example.ecommerce.payment.dto.request;

import com.example.ecommerce.payment.entity.PaymentMethod;
import com.example.ecommerce.payment.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Filter parameters for searching and listing payment transactions.
 */
@Schema(description = "Payment search and filter criteria")
public class PaymentFilterRequest {

    @Schema(description = "Keyword search matching transaction ID or order number", example = "TXN-2026")
    private String search;

    @Schema(description = "Filter by payment method", example = "BKASH")
    private PaymentMethod paymentMethod;

    @Schema(description = "Filter by payment status", example = "SUCCESS")
    private PaymentStatus paymentStatus;

    @Schema(description = "Filter payments created on or after date")
    private Instant startDate;

    @Schema(description = "Filter payments created on or before date")
    private Instant endDate;

    public PaymentFilterRequest() {
    }

    public PaymentFilterRequest(String search, PaymentMethod paymentMethod, PaymentStatus paymentStatus, Instant startDate, Instant endDate) {
        this.search = search;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }

    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }

    public static PaymentFilterRequestBuilder builder() { return new PaymentFilterRequestBuilder(); }

    public static class PaymentFilterRequestBuilder {
        private String search;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private Instant startDate;
        private Instant endDate;

        PaymentFilterRequestBuilder() {}

        public PaymentFilterRequestBuilder search(String search) { this.search = search; return this; }
        public PaymentFilterRequestBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public PaymentFilterRequestBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public PaymentFilterRequestBuilder startDate(Instant startDate) { this.startDate = startDate; return this; }
        public PaymentFilterRequestBuilder endDate(Instant endDate) { this.endDate = endDate; return this; }

        public PaymentFilterRequest build() {
            return new PaymentFilterRequest(search, paymentMethod, paymentStatus, startDate, endDate);
        }
    }
}
