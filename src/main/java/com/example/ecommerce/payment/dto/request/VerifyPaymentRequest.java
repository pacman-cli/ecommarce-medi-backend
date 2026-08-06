package com.example.ecommerce.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Payload for verifying payment transaction with gateway.
 */
@Schema(description = "Payload for payment verification")
public class VerifyPaymentRequest {

    @NotBlank(message = "Transaction ID is required")
    @Schema(description = "Internal transaction ID", example = "TXN-20260804-98421")
    private String transactionId;

    @Schema(description = "Gateway reference transaction ID", example = "SSL-VAL-88771122")
    private String gatewayTransactionId;

    @Schema(description = "SSLCommerz validation ID (val_id)", example = "2608041840001")
    private String valId;

    public VerifyPaymentRequest() {
    }

    public VerifyPaymentRequest(String transactionId, String gatewayTransactionId, String valId) {
        this.transactionId = transactionId;
        this.gatewayTransactionId = gatewayTransactionId;
        this.valId = valId;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public String getValId() { return valId; }
    public void setValId(String valId) { this.valId = valId; }

    public static VerifyPaymentRequestBuilder builder() { return new VerifyPaymentRequestBuilder(); }

    public static class VerifyPaymentRequestBuilder {
        private String transactionId;
        private String gatewayTransactionId;
        private String valId;

        VerifyPaymentRequestBuilder() {}

        public VerifyPaymentRequestBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public VerifyPaymentRequestBuilder gatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; return this; }
        public VerifyPaymentRequestBuilder valId(String valId) { this.valId = valId; return this; }

        public VerifyPaymentRequest build() {
            return new VerifyPaymentRequest(transactionId, gatewayTransactionId, valId);
        }
    }
}
