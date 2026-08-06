package com.example.ecommerce.payment.dto.request;

import com.example.ecommerce.payment.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload for initiating an online or cash payment transaction.
 */
@Schema(description = "Payload for initiating payment")
public class InitiatePaymentRequest {

    @NotNull(message = "Order ID is required")
    @Schema(description = "Order ID to pay for", example = "100")
    private Long orderId;

    @NotNull(message = "Payment method is required")
    @Schema(description = "Target payment gateway method", example = "SSLCOMMERZ")
    private PaymentMethod paymentMethod;

    @Size(max = 500, message = "Return URL must not exceed 500 characters")
    @Schema(description = "Custom success return URL", example = "https://example.com/checkout/success")
    private String returnUrl;

    @Size(max = 500, message = "Cancel URL must not exceed 500 characters")
    @Schema(description = "Custom cancel/failure return URL", example = "https://example.com/checkout/cancel")
    private String cancelUrl;

    public InitiatePaymentRequest() {
    }

    public InitiatePaymentRequest(Long orderId, PaymentMethod paymentMethod, String returnUrl, String cancelUrl) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.returnUrl = returnUrl;
        this.cancelUrl = cancelUrl;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }

    public String getCancelUrl() { return cancelUrl; }
    public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }

    public static InitiatePaymentRequestBuilder builder() { return new InitiatePaymentRequestBuilder(); }

    public static class InitiatePaymentRequestBuilder {
        private Long orderId;
        private PaymentMethod paymentMethod;
        private String returnUrl;
        private String cancelUrl;

        InitiatePaymentRequestBuilder() {}

        public InitiatePaymentRequestBuilder orderId(Long orderId) { this.orderId = orderId; return this; }
        public InitiatePaymentRequestBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public InitiatePaymentRequestBuilder returnUrl(String returnUrl) { this.returnUrl = returnUrl; return this; }
        public InitiatePaymentRequestBuilder cancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; return this; }

        public InitiatePaymentRequest build() {
            return new InitiatePaymentRequest(orderId, paymentMethod, returnUrl, cancelUrl);
        }
    }
}
