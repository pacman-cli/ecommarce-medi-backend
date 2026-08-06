package com.example.ecommerce.purchase.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.inventory.entity.Warehouse;
import com.example.ecommerce.purchase.dto.enums.PurchasePaymentStatus;
import com.example.ecommerce.purchase.dto.enums.PurchaseStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Purchase Order aggregate root entity tracking procurement requests, supplier bindings,
 * warehouse destinations, receiving progress, invoice amounts, and payment statuses.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "purchase_orders",
        indexes = {
                @Index(name = "idx_po_number", columnList = "po_number"),
                @Index(name = "idx_po_supplier", columnList = "supplier_id"),
                @Index(name = "idx_po_status", columnList = "status")
        }
)
@SQLDelete(sql = "UPDATE purchase_orders SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class PurchaseOrder extends BaseEntity {

    @Column(name = "po_number", nullable = false, length = 50, unique = true)
    private String poNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private PurchaseStatus status = PurchaseStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    @Builder.Default
    private PurchasePaymentStatus paymentStatus = PurchasePaymentStatus.UNPAID;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "invoice_number", length = 100)
    private String invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "shipping_cost", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(length = 500)
    private String notes;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PurchaseItem> items = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void addItem(PurchaseItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        item.setPurchaseOrder(this);
        this.items.add(item);
    }
}
