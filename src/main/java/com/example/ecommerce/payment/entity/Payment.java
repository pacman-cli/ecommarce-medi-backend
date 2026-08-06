package com.example.ecommerce.payment.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.order.entity.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise payment entity supporting Cash on Delivery, SSLCommerz, bKash, Nagad gateways,
 * transaction logging, verification and partial/full refunds.
 */
@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_payments_transaction_id", columnNames = "transaction_id")
        }
)
@SQLDelete(sql = "UPDATE payments SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Payment extends BaseEntity {

    @Column(name = "transaction_id", nullable = false, length = 50)
    private String transactionId;

    @Column(name = "gateway_transaction_id", length = 100)
    private String gatewayTransactionId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "refunded_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    @Column(nullable = false, length = 10)
    private String currency = "BDT";

    @Column(name = "redirect_url", length = 500)
    private String redirectUrl;

    @Column(name = "failure_reason", length = 250)
    private String failureReason;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "refunded_at")
    private Instant refundedAt;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentTransaction> transactions = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Payment() {
    }

    public void addTransaction(TransactionType type, String gateway, String gatewayTxnId, String req, String resp, PaymentStatus status) {
        if (this.transactions == null) {
            this.transactions = new ArrayList<>();
        }
        PaymentTransaction txn = PaymentTransaction.builder()
                .payment(this)
                .transactionType(type)
                .gateway(gateway)
                .gatewayTransactionId(gatewayTxnId)
                .requestPayload(req)
                .responsePayload(resp)
                .status(status)
                .createdAt(Instant.now())
                .build();
        this.transactions.add(txn);
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

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

    public List<PaymentTransaction> getTransactions() { return transactions; }
    public void setTransactions(List<PaymentTransaction> transactions) { this.transactions = transactions; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static PaymentBuilder builder() { return new PaymentBuilder(); }

    public static class PaymentBuilder {
        private String transactionId;
        private String gatewayTransactionId;
        private Order order;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus = PaymentStatus.PENDING;
        private BigDecimal amount = BigDecimal.ZERO;
        private BigDecimal refundedAmount = BigDecimal.ZERO;
        private String currency = "BDT";
        private String redirectUrl;
        private String failureReason;
        private Instant paidAt;
        private Instant refundedAt;
        private List<PaymentTransaction> transactions = new ArrayList<>();
        private boolean deleted = false;
        private Instant deletedAt;

        PaymentBuilder() {}

        public PaymentBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public PaymentBuilder gatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; return this; }
        public PaymentBuilder order(Order order) { this.order = order; return this; }
        public PaymentBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public PaymentBuilder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public PaymentBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public PaymentBuilder refundedAmount(BigDecimal refundedAmount) { this.refundedAmount = refundedAmount; return this; }
        public PaymentBuilder currency(String currency) { this.currency = currency; return this; }
        public PaymentBuilder redirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; return this; }
        public PaymentBuilder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public PaymentBuilder paidAt(Instant paidAt) { this.paidAt = paidAt; return this; }
        public PaymentBuilder refundedAt(Instant refundedAt) { this.refundedAt = refundedAt; return this; }
        public PaymentBuilder transactions(List<PaymentTransaction> transactions) { this.transactions = transactions; return this; }
        public PaymentBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public PaymentBuilder deletedAt(Instant deletedAt) { this.deletedAt = deletedAt; return this; }

        public Payment build() {
            Payment p = new Payment();
            p.setTransactionId(transactionId);
            p.setGatewayTransactionId(gatewayTransactionId);
            p.setOrder(order);
            p.setPaymentMethod(paymentMethod);
            p.setPaymentStatus(paymentStatus != null ? paymentStatus : PaymentStatus.PENDING);
            p.setAmount(amount != null ? amount : BigDecimal.ZERO);
            p.setRefundedAmount(refundedAmount != null ? refundedAmount : BigDecimal.ZERO);
            p.setCurrency(currency != null ? currency : "BDT");
            p.setRedirectUrl(redirectUrl);
            p.setFailureReason(failureReason);
            p.setPaidAt(paidAt);
            p.setRefundedAt(refundedAt);
            p.setTransactions(transactions != null ? transactions : new ArrayList<>());
            p.setDeleted(deleted);
            p.setDeletedAt(deletedAt);
            return p;
        }
    }
}
