package com.example.ecommerce.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Log entry capturing raw gateway API requests, responses and webhooks.
 */
@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 30)
    private TransactionType transactionType;

    @Column(nullable = false, length = 50)
    private String gateway;

    @Column(name = "gateway_transaction_id", length = 100)
    private String gatewayTransactionId;

    @Lob
    @Column(name = "request_payload", columnDefinition = "TEXT")
    private String requestPayload;

    @Lob
    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public PaymentTransaction() {
    }

    public PaymentTransaction(Long id, Payment payment, TransactionType transactionType, String gateway, String gatewayTransactionId, String requestPayload, String responsePayload, PaymentStatus status, Instant createdAt) {
        this.id = id;
        this.payment = payment;
        this.transactionType = transactionType;
        this.gateway = gateway;
        this.gatewayTransactionId = gatewayTransactionId;
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public String getGateway() { return gateway; }
    public void setGateway(String gateway) { this.gateway = gateway; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public String getRequestPayload() { return requestPayload; }
    public void setRequestPayload(String requestPayload) { this.requestPayload = requestPayload; }

    public String getResponsePayload() { return responsePayload; }
    public void setResponsePayload(String responsePayload) { this.responsePayload = responsePayload; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static PaymentTransactionBuilder builder() { return new PaymentTransactionBuilder(); }

    public static class PaymentTransactionBuilder {
        private Long id;
        private Payment payment;
        private TransactionType transactionType;
        private String gateway;
        private String gatewayTransactionId;
        private String requestPayload;
        private String responsePayload;
        private PaymentStatus status;
        private Instant createdAt = Instant.now();

        PaymentTransactionBuilder() {}

        public PaymentTransactionBuilder id(Long id) { this.id = id; return this; }
        public PaymentTransactionBuilder payment(Payment payment) { this.payment = payment; return this; }
        public PaymentTransactionBuilder transactionType(TransactionType transactionType) { this.transactionType = transactionType; return this; }
        public PaymentTransactionBuilder gateway(String gateway) { this.gateway = gateway; return this; }
        public PaymentTransactionBuilder gatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; return this; }
        public PaymentTransactionBuilder requestPayload(String requestPayload) { this.requestPayload = requestPayload; return this; }
        public PaymentTransactionBuilder responsePayload(String responsePayload) { this.responsePayload = responsePayload; return this; }
        public PaymentTransactionBuilder status(PaymentStatus status) { this.status = status; return this; }
        public PaymentTransactionBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public PaymentTransaction build() {
            return new PaymentTransaction(id, payment, transactionType, gateway, gatewayTransactionId, requestPayload, responsePayload, status, createdAt);
        }
    }
}
