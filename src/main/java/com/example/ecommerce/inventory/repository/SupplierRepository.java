package com.example.ecommerce.inventory.repository;

import org.springframework.stereotype.Repository;

/**
 * Backward compatibility inventory repository interface extending {@link com.example.ecommerce.supplier.repository.SupplierRepository}.
 */
@Repository("inventorySupplierRepository")
public interface SupplierRepository extends com.example.ecommerce.supplier.repository.SupplierRepository {
}
