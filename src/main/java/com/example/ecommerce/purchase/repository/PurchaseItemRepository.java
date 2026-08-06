package com.example.ecommerce.purchase.repository;

import com.example.ecommerce.purchase.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link PurchaseItem} line item entities.
 */
@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

    List<PurchaseItem> findByPurchaseOrderId(Long purchaseOrderId);

    List<PurchaseItem> findByProductId(Long productId);
}
