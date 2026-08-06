package com.example.ecommerce.payment.dto.response;

import com.example.ecommerce.payment.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Result returned by payment verification checks.
 */
@Schema(description = "Payment verification response")
public class PaymentVerificationResponse {

    @Schema(description = "Internal transaction ID", example = "TXN-20260804-98421")
    private String transactionId;

    @Schema(description = "Gateway transaction ID", example = "SSL-VAL-88771122")
    private String gatewayTransactionId;

    @Schema(description = "Associated order number", example = "ORD-20260804-98421")
    private String orderNumber;

    @Schema(description = "Updated payment status", example = "SUCCESS")
    private PaymentStatus paymentStatus;

    @Schema(description = "Verification flag", example = "true")
    private boolean paid;

    @Schema(description = "Verification outcome message", example = "Payment verified successfully with gateway")
    private String message;

    public PaymentVerificationResponse() {
    }

    public PaymentVerificationResponse(String transactionId, String gatewayTransactionId, String orderNumber, PaymentStatus paymentStatus, boolean paid, String message) {
        this.transactionId = transactionId;
        this.gatewayTransactionId = gatewayTransactionId;
        this.orderNumber = orderNumber;
        this.paymentStatus = paymentStatus;
        this.paid = paid;
        this.message = message;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static PaymentVerificationResponseBuilder builder() { return new PaymentVerificationResponseBuilder(); }

    public static class PaymentVerificationResponseBuilder {
        private String transactionId;
        private String gatewayTransactionId;
        private String orderNumber;
        private PaymentStatus paymentStatus;
        private boolean paid;
        private String message;

        PaymentVerificationResponseBuilder() {}

        public PaymentVerificationResponseBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public PaymentVerificationResponseBuilder gatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; return this; }
        public PaymentVerificationResponseBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public PaymentVerificationResponseBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public PaymentVerificationResponseBuilder paid(boolean paid) { this.paid = paid; return this; }
        public PaymentVerificationResponseBuilder message(String message) { this.message = message; return this; }

        public PaymentVerificationResponse build() {
            return new PaymentVerificationResponse(transactionId, gatewayTransactionId, orderNumber, paymentStatus, paid, message);
        }
    }
}
