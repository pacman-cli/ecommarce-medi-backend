package com.example.ecommerce.payment.dto.response;

import com.example.ecommerce.payment.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Refund processing result response DTO.
 */
@Schema(description = "Payment refund outcome response")
public class RefundResponse {

    @Schema(description = "Payment ID", example = "50")
    private Long paymentId;

    @Schema(description = "Transaction ID", example = "TXN-20260804-98421")
    private String transactionId;

    @Schema(description = "Amount refunded in this operation", example = "25.00")
    private BigDecimal refundedAmount;

    @Schema(description = "Cumulative total refunded amount", example = "25.00")
    private BigDecimal totalRefunded;

    @Schema(description = "Updated payment status", example = "REFUNDED")
    private PaymentStatus paymentStatus;

    @Schema(description = "Refund outcome message", example = "Refund processed successfully with gateway")
    private String message;

    public RefundResponse() {
    }

    public RefundResponse(Long paymentId, String transactionId, BigDecimal refundedAmount, BigDecimal totalRefunded, PaymentStatus paymentStatus, String message) {
        this.paymentId = paymentId;
        this.transactionId = transactionId;
        this.refundedAmount = refundedAmount;
        this.totalRefunded = totalRefunded;
        this.paymentStatus = paymentStatus;
        this.message = message;
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public BigDecimal getRefundedAmount() { return refundedAmount; }
    public void setRefundedAmount(BigDecimal refundedAmount) { this.refundedAmount = refundedAmount; }

    public BigDecimal getTotalRefunded() { return totalRefunded; }
    public void setTotalRefunded(BigDecimal totalRefunded) { this.totalRefunded = totalRefunded; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static RefundResponseBuilder builder() { return new RefundResponseBuilder(); }

    public static class RefundResponseBuilder {
        private Long paymentId;
        private String transactionId;
        private BigDecimal refundedAmount;
        private BigDecimal totalRefunded;
        private PaymentStatus paymentStatus;
        private String message;

        RefundResponseBuilder() {}

        public RefundResponseBuilder paymentId(Long paymentId) { this.paymentId = paymentId; return this; }
        public RefundResponseBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public RefundResponseBuilder refundedAmount(BigDecimal refundedAmount) { this.refundedAmount = refundedAmount; return this; }
        public RefundResponseBuilder totalRefunded(BigDecimal totalRefunded) { this.totalRefunded = totalRefunded; return this; }
        public RefundResponseBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public RefundResponseBuilder message(String message) { this.message = message; return this; }

        public RefundResponse build() {
            return new RefundResponse(paymentId, transactionId, refundedAmount, totalRefunded, paymentStatus, message);
        }
    }
}
