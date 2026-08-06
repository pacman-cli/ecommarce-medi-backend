package com.example.ecommerce.payment.dto.response;

import com.example.ecommerce.payment.entity.PaymentMethod;
import com.example.ecommerce.payment.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Detailed master payment response DTO.
 */
@Schema(description = "Full payment entity response")
public class PaymentResponse {

    @Schema(description = "Payment ID", example = "50")
    private Long id;

    @Schema(description = "Internal unique transaction ID", example = "TXN-20260804-98421")
    private String transactionId;

    @Schema(description = "Gateway reference transaction ID", example = "SSL-VAL-88771122")
    private String gatewayTransactionId;

    @Schema(description = "Associated order ID", example = "100")
    private Long orderId;

    @Schema(description = "Associated order number", example = "ORD-20260804-98421")
    private String orderNumber;

    @Schema(description = "Payment method", example = "BKASH")
    private PaymentMethod paymentMethod;

    @Schema(description = "Payment status", example = "SUCCESS")
    private PaymentStatus paymentStatus;

    @Schema(description = "Transaction amount", example = "94.50")
    private BigDecimal amount;

    @Schema(description = "Total refunded amount", example = "0.00")
    private BigDecimal refundedAmount;

    @Schema(description = "Currency", example = "BDT")
    private String currency;

    @Schema(description = "Gateway redirect checkout URL", example = "https://sandbox.bkash.com/checkout/...")
    private String redirectUrl;

    @Schema(description = "Failure reason if failed")
    private String failureReason;

    @Schema(description = "Timestamp when payment was completed")
    private Instant paidAt;

    @Schema(description = "Timestamp when refund was executed")
    private Instant refundedAt;

    @Schema(description = "Payment record creation timestamp")
    private Instant createdAt;

    public PaymentResponse() {
    }

    public PaymentResponse(Long id, String transactionId, String gatewayTransactionId, Long orderId, String orderNumber, PaymentMethod paymentMethod, PaymentStatus paymentStatus, BigDecimal amount, BigDecimal refundedAmount, String currency, String redirectUrl, String failureReason, Instant paidAt, Instant refundedAt, Instant createdAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.gatewayTransactionId = gatewayTransactionId;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.refundedAmount = refundedAmount;
        this.currency = currency;
        this.redirectUrl = redirectUrl;
        this.failureReason = failureReason;
        this.paidAt = paidAt;
        this.refundedAt = refundedAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getRefundedAmount() { return refundedAmount; }
    public void setRefundedAmount(BigDecimal refundedAmount) { this.refundedAmount = refundedAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public Instant getPaidAt() { return paidAt; }
    public void setPaidAt(Instant paidAt) { this.paidAt = paidAt; }

    public Instant getRefundedAt() { return refundedAt; }
    public void setRefundedAt(Instant refundedAt) { this.refundedAt = refundedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static PaymentResponseBuilder builder() { return new PaymentResponseBuilder(); }

    public static class PaymentResponseBuilder {
        private Long id;
        private String transactionId;
        private String gatewayTransactionId;
        private Long orderId;
        private String orderNumber;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private BigDecimal amount;
        private BigDecimal refundedAmount;
        private String currency;
        private String redirectUrl;
        private String failureReason;
        private Instant paidAt;
        private Instant refundedAt;
        private Instant createdAt;

        PaymentResponseBuilder() {}

        public PaymentResponseBuilder id(Long id) { this.id = id; return this; }
        public PaymentResponseBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public PaymentResponseBuilder gatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; return this; }
        public PaymentResponseBuilder orderId(Long orderId) { this.orderId = orderId; return this; }
        public PaymentResponseBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public PaymentResponseBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public PaymentResponseBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public PaymentResponseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public PaymentResponseBuilder refundedAmount(BigDecimal refundedAmount) { this.refundedAmount = refundedAmount; return this; }
        public PaymentResponseBuilder currency(String currency) { this.currency = currency; return this; }
        public PaymentResponseBuilder redirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; return this; }
        public PaymentResponseBuilder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public PaymentResponseBuilder paidAt(Instant paidAt) { this.paidAt = paidAt; return this; }
        public PaymentResponseBuilder refundedAt(Instant refundedAt) { this.refundedAt = refundedAt; return this; }
        public PaymentResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public PaymentResponse build() {
            return new PaymentResponse(id, transactionId, gatewayTransactionId, orderId, orderNumber, paymentMethod, paymentStatus, amount, refundedAmount, currency, redirectUrl, failureReason, paidAt, refundedAt, createdAt);
        }
    }
}
