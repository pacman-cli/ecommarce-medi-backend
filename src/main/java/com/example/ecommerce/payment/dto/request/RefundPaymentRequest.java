package com.example.ecommerce.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Payload for executing partial or full payment refund.
 */
@Schema(description = "Payload for processing payment refund")
public class RefundPaymentRequest {

    @NotNull(message = "Payment ID is required")
    @Schema(description = "Payment ID to refund", example = "50")
    private Long paymentId;

    @NotNull(message = "Refund amount is required")
    @DecimalMin(value = "0.01", message = "Refund amount must be greater than 0")
    @Schema(description = "Monetary amount to refund", example = "25.00")
    private BigDecimal amount;

    @NotBlank(message = "Refund reason is required")
    @Size(max = 250, message = "Reason must not exceed 250 characters")
    @Schema(description = "Reason for refund", example = "Customer returned defective item")
    private String reason;

    public RefundPaymentRequest() {
    }

    public RefundPaymentRequest(Long paymentId, BigDecimal amount, String reason) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.reason = reason;
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public static RefundPaymentRequestBuilder builder() { return new RefundPaymentRequestBuilder(); }

    public static class RefundPaymentRequestBuilder {
        private Long paymentId;
        private BigDecimal amount;
        private String reason;

        RefundPaymentRequestBuilder() {}

        public RefundPaymentRequestBuilder paymentId(Long paymentId) { this.paymentId = paymentId; return this; }
        public RefundPaymentRequestBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public RefundPaymentRequestBuilder reason(String reason) { this.reason = reason; return this; }

        public RefundPaymentRequest build() {
            return new RefundPaymentRequest(paymentId, amount, reason);
        }
    }
}
