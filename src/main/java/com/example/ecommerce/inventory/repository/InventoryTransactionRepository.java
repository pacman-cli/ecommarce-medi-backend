package com.example.ecommerce.inventory.repository;

import com.example.ecommerce.inventory.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link InventoryTransaction} audit logs.
 */
@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long>, JpaSpecificationExecutor<InventoryTransaction> {

    List<InventoryTransaction> findByProductId(Long productId);

    List<InventoryTransaction> findByStockBatchId(Long stockBatchId);

    List<InventoryTransaction> findByWarehouseId(Long warehouseId);

    List<InventoryTransaction> findBySupplierId(Long supplierId);
}
