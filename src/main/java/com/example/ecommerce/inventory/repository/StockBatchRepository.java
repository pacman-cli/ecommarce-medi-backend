package com.example.ecommerce.inventory.repository;

import com.example.ecommerce.inventory.entity.BatchStatus;
import com.example.ecommerce.inventory.entity.StockBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link StockBatch} entities.
 */
@Repository
public interface StockBatchRepository extends JpaRepository<StockBatch, Long>, JpaSpecificationExecutor<StockBatch> {

    Optional<StockBatch> findByIdAndDeletedFalse(Long id);

    Optional<StockBatch> findByBarcodeAndDeletedFalse(String barcode);

    Optional<StockBatch> findByQrCodeAndDeletedFalse(String qrCode);

    Optional<StockBatch> findByBatchNumberAndProductIdAndWarehouseIdAndDeletedFalse(String batchNumber, Long productId, Long warehouseId);

    List<StockBatch> findByProductIdAndDeletedFalse(Long productId);

    List<StockBatch> findByWarehouseIdAndDeletedFalse(Long warehouseId);

    List<StockBatch> findByStatusAndDeletedFalse(BatchStatus status);

    List<StockBatch> findByExpiryDateBeforeAndDeletedFalse(LocalDate date);

    List<StockBatch> findBySupplierIdAndDeletedFalse(Long supplierId);

    boolean existsByBatchNumberAndProductIdAndWarehouseIdAndDeletedFalse(String batchNumber, Long productId, Long warehouseId);

    @Query("SELECT COALESCE(SUM(b.availableQuantity), 0) FROM StockBatch b WHERE b.product.id = :productId AND b.deleted = false AND b.status <> 'EXPIRED'")
    Integer sumAvailableQuantityByProductId(@Param("productId") Long productId);

    @org.springframework.data.jpa.repository.Modifying
    @Query("UPDATE StockBatch b SET b.status = com.example.ecommerce.inventory.entity.BatchStatus.EXPIRED "
           + "WHERE b.deleted = false AND b.status <> com.example.ecommerce.inventory.entity.BatchStatus.EXPIRED "
           + "AND b.expiryDate IS NOT NULL AND b.expiryDate < :date")
    int markExpiredBatches(@Param("date") LocalDate date);
}
