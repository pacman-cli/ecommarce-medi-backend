package com.example.ecommerce.purchase.repository;

import com.example.ecommerce.purchase.dto.enums.PurchaseStatus;
import com.example.ecommerce.purchase.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link PurchaseOrder} aggregate entities.
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {

    Optional<PurchaseOrder> findByIdAndDeletedFalse(Long id);

    Optional<PurchaseOrder> findByPoNumberAndDeletedFalse(String poNumber);

    List<PurchaseOrder> findBySupplierIdAndDeletedFalse(Long supplierId);

    List<PurchaseOrder> findByWarehouseIdAndDeletedFalse(Long warehouseId);

    List<PurchaseOrder> findByStatusAndDeletedFalse(PurchaseStatus status);

    boolean existsByPoNumberAndDeletedFalse(String poNumber);
}
