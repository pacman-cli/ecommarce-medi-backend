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
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Inventory stock batch entity tracking batch numbers, expiration, purchase/selling pricing,
 * barcodes, QR codes, and warehouse/supplier bindings.
 */
@Entity
@Table(
        name = "stock_batches",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_stock_batches_number_product_warehouse", columnNames = {"batch_number", "product_id", "warehouse_id"})
        }
)
@SQLDelete(sql = "UPDATE stock_batches SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class StockBatch extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "batch_number", nullable = false, length = 100)
    private String batchNumber;

    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "purchase_price", precision = 19, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "selling_price", precision = 19, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity = 0;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    @Column(length = 100)
    private String barcode;

    @Column(name = "qr_code", length = 300)
    private String qrCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BatchStatus status = BatchStatus.AVAILABLE;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public StockBatch() {
    }

    public void recalculateAvailable() {
        int avail = (quantity != null ? quantity : 0) - (reservedQuantity != null ? reservedQuantity : 0);
        this.availableQuantity = Math.max(0, avail);
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            this.status = BatchStatus.EXPIRED;
        } else if (this.availableQuantity <= 0) {
            this.status = BatchStatus.DEPLETED;
        } else if (this.availableQuantity <= 5) {
            this.status = BatchStatus.LOW_STOCK;
        } else if (this.status != BatchStatus.QUARANTINED) {
            this.status = BatchStatus.AVAILABLE;
        }
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public LocalDate getManufacturingDate() { return manufacturingDate; }
    public void setManufacturingDate(LocalDate manufacturingDate) { this.manufacturingDate = manufacturingDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public BatchStatus getStatus() { return status; }
    public void setStatus(BatchStatus status) { this.status = status; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static StockBatchBuilder builder() { return new StockBatchBuilder(); }

    public static class StockBatchBuilder {
        private Product product;
        private Warehouse warehouse;
        private Supplier supplier;
        private String batchNumber;
        private LocalDate manufacturingDate;
        private LocalDate expiryDate;
        private BigDecimal purchasePrice;
        private BigDecimal sellingPrice;
        private Integer quantity = 0;
        private Integer availableQuantity = 0;
        private Integer reservedQuantity = 0;
        private String barcode;
        private String qrCode;
        private BatchStatus status = BatchStatus.AVAILABLE;
        private boolean deleted = false;
        private Instant deletedAt;

        StockBatchBuilder() {}

        public StockBatchBuilder product(Product product) { this.product = product; return this; }
        public StockBatchBuilder warehouse(Warehouse warehouse) { this.warehouse = warehouse; return this; }
        public StockBatchBuilder supplier(Supplier supplier) { this.supplier = supplier; return this; }
        public StockBatchBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public StockBatchBuilder manufacturingDate(LocalDate manufacturingDate) { this.manufacturingDate = manufacturingDate; return this; }
        public StockBatchBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public StockBatchBuilder purchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; return this; }
        public StockBatchBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public StockBatchBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public StockBatchBuilder availableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; return this; }
        public StockBatchBuilder reservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }
        public StockBatchBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public StockBatchBuilder qrCode(String qrCode) { this.qrCode = qrCode; return this; }
        public StockBatchBuilder status(BatchStatus status) { this.status = status; return this; }
        public StockBatchBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public StockBatchBuilder deletedAt(Instant deletedAt) { this.deletedAt = deletedAt; return this; }

        public StockBatch build() {
            StockBatch b = new StockBatch();
            b.setProduct(product);
            b.setWarehouse(warehouse);
            b.setSupplier(supplier);
            b.setBatchNumber(batchNumber);
            b.setManufacturingDate(manufacturingDate);
            b.setExpiryDate(expiryDate);
            b.setPurchasePrice(purchasePrice);
            b.setSellingPrice(sellingPrice);
            b.setQuantity(quantity != null ? quantity : 0);
            b.setAvailableQuantity(availableQuantity != null ? availableQuantity : 0);
            b.setReservedQuantity(reservedQuantity != null ? reservedQuantity : 0);
            b.setBarcode(barcode);
            b.setQrCode(qrCode);
            b.setStatus(status != null ? status : BatchStatus.AVAILABLE);
            b.setDeleted(deleted);
            b.setDeletedAt(deletedAt);
            b.recalculateAvailable();
            return b;
        }
    }
}
