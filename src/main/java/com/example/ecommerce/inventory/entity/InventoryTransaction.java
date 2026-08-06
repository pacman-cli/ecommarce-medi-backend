package com.example.ecommerce.inventory.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Audit log recording every inventory movement, purchase inbound, sale outbound or stock adjustment.
 */
@Entity
@Table(name = "inventory_transactions")
public class InventoryTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_batch_id")
    private StockBatch stockBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 30)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_value", precision = 19, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @Column(length = 500)
    private String reason;

    @Column(name = "performed_by", length = 100)
    private String performedBy;

    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate = Instant.now();

    public InventoryTransaction() {
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public StockBatch getStockBatch() { return stockBatch; }
    public void setStockBatch(StockBatch stockBatch) { this.stockBatch = stockBatch; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public Instant getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Instant transactionDate) { this.transactionDate = transactionDate; }

    public static InventoryTransactionBuilder builder() { return new InventoryTransactionBuilder(); }

    public static class InventoryTransactionBuilder {
        private Product product;
        private StockBatch stockBatch;
        private Warehouse warehouse;
        private Supplier supplier;
        private TransactionType transactionType;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalValue;
        private String referenceNumber;
        private String reason;
        private String performedBy;
        private Instant transactionDate = Instant.now();

        InventoryTransactionBuilder() {}

        public InventoryTransactionBuilder product(Product product) { this.product = product; return this; }
        public InventoryTransactionBuilder stockBatch(StockBatch stockBatch) { this.stockBatch = stockBatch; return this; }
        public InventoryTransactionBuilder warehouse(Warehouse warehouse) { this.warehouse = warehouse; return this; }
        public InventoryTransactionBuilder supplier(Supplier supplier) { this.supplier = supplier; return this; }
        public InventoryTransactionBuilder transactionType(TransactionType transactionType) { this.transactionType = transactionType; return this; }
        public InventoryTransactionBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public InventoryTransactionBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public InventoryTransactionBuilder totalValue(BigDecimal totalValue) { this.totalValue = totalValue; return this; }
        public InventoryTransactionBuilder referenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }
        public InventoryTransactionBuilder reason(String reason) { this.reason = reason; return this; }
        public InventoryTransactionBuilder performedBy(String performedBy) { this.performedBy = performedBy; return this; }
        public InventoryTransactionBuilder transactionDate(Instant transactionDate) { this.transactionDate = transactionDate; return this; }

        public InventoryTransaction build() {
            InventoryTransaction t = new InventoryTransaction();
            t.setProduct(product);
            t.setStockBatch(stockBatch);
            t.setWarehouse(warehouse);
            t.setSupplier(supplier);
            t.setTransactionType(transactionType);
            t.setQuantity(quantity);
            t.setUnitPrice(unitPrice);
            t.setTotalValue(totalValue);
            t.setReferenceNumber(referenceNumber);
            t.setReason(reason);
            t.setPerformedBy(performedBy);
            t.setTransactionDate(transactionDate != null ? transactionDate : Instant.now());
            return t;
        }
    }
}
