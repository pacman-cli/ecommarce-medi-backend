package com.example.ecommerce.payment.dto.response;

import com.example.ecommerce.payment.entity.PaymentMethod;
import com.example.ecommerce.payment.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Result returned upon initiating payment with gateway redirect details.
 */
@Schema(description = "Payment initiation result response")
public class PaymentInitiationResponse {

    @Schema(description = "Payment ID", example = "50")
    private Long paymentId;

    @Schema(description = "Internal transaction ID", example = "TXN-20260804-98421")
    private String transactionId;

    @Schema(description = "Associated order number", example = "ORD-20260804-98421")
    private String orderNumber;

    @Schema(description = "Selected payment method", example = "SSLCOMMERZ")
    private PaymentMethod paymentMethod;

    @Schema(description = "Initial payment status", example = "PENDING")
    private PaymentStatus paymentStatus;

    @Schema(description = "Payment amount", example = "94.50")
    private BigDecimal amount;

    @Schema(description = "Currency", example = "BDT")
    private String currency;

    @Schema(description = "Gateway checkout/redirect URL (null for COD)", example = "https://sandbox.sslcommerz.com/gwprocess/v4/gw.php?Q=pay...")
    private String gatewayRedirectUrl;

    public PaymentInitiationResponse() {
    }

    public PaymentInitiationResponse(Long paymentId, String transactionId, String orderNumber, PaymentMethod paymentMethod, PaymentStatus paymentStatus, BigDecimal amount, String currency, String gatewayRedirectUrl) {
        this.paymentId = paymentId;
        this.transactionId = transactionId;
        this.orderNumber = orderNumber;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.currency = currency;
        this.gatewayRedirectUrl = gatewayRedirectUrl;
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getGatewayRedirectUrl() { return gatewayRedirectUrl; }
    public void setGatewayRedirectUrl(String gatewayRedirectUrl) { this.gatewayRedirectUrl = gatewayRedirectUrl; }

    public static PaymentInitiationResponseBuilder builder() { return new PaymentInitiationResponseBuilder(); }

    public static class PaymentInitiationResponseBuilder {
        private Long paymentId;
        private String transactionId;
        private String orderNumber;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private BigDecimal amount;
        private String currency;
        private String gatewayRedirectUrl;

        PaymentInitiationResponseBuilder() {}

        public PaymentInitiationResponseBuilder paymentId(Long paymentId) { this.paymentId = paymentId; return this; }
        public PaymentInitiationResponseBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public PaymentInitiationResponseBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public PaymentInitiationResponseBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public PaymentInitiationResponseBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public PaymentInitiationResponseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public PaymentInitiationResponseBuilder currency(String currency) { this.currency = currency; return this; }
        public PaymentInitiationResponseBuilder gatewayRedirectUrl(String gatewayRedirectUrl) { this.gatewayRedirectUrl = gatewayRedirectUrl; return this; }

        public PaymentInitiationResponse build() {
            return new PaymentInitiationResponse(paymentId, transactionId, orderNumber, paymentMethod, paymentStatus, amount, currency, gatewayRedirectUrl);
        }
    }
}
