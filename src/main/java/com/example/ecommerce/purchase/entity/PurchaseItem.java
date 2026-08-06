package com.example.ecommerce.purchase.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Line item entity associated with a {@link PurchaseOrder} specifying requested product,
 * ordered quantity, received quantity, and purchase unit cost.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_items")
public class PurchaseItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "ordered_quantity", nullable = false)
    @Builder.Default
    private Integer orderedQuantity = 1;

    @Column(name = "received_quantity", nullable = false)
    @Builder.Default
    private Integer receivedQuantity = 0;

    @Column(name = "unit_cost", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Column(name = "total_cost", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(length = 250)
    private String notes;
}
