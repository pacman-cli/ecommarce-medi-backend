package com.example.ecommerce.supplier.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.supplier.dto.enums.SupplierStatus;
import com.example.ecommerce.supplier.dto.request.SupplierFilterRequest;
import com.example.ecommerce.supplier.dto.request.SupplierRequest;
import com.example.ecommerce.supplier.dto.response.SupplierDetailResponse;
import com.example.ecommerce.supplier.dto.response.SupplierProductSummaryResponse;
import com.example.ecommerce.supplier.dto.response.SupplierPurchaseHistoryResponse;
import com.example.ecommerce.supplier.dto.response.SupplierResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface defining business operations for vendor supplier profiles,
 * status lifecycles, supplied products, and purchase receiving history.
 */
public interface SupplierService {

    /**
     * Creates a new supplier vendor profile.
     */
    SupplierResponse createSupplier(SupplierRequest request);

    /**
     * Updates an existing supplier vendor profile.
     */
    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    /**
     * Retrieves supplier profile by ID.
     */
    SupplierResponse getSupplierById(Long id);

    /**
     * Retrieves detailed supplier profile with products supplied and purchase history metrics.
     */
    SupplierDetailResponse getSupplierDetailById(Long id);

    /**
     * Retrieves paginated suppliers matching search filter criteria.
     */
    PageResponse<SupplierResponse> getSuppliers(SupplierFilterRequest filter, Pageable pageable);

    /**
     * Retrieves list of all active supplier profiles.
     */
    List<SupplierResponse> getAllActiveSuppliers();

    /**
     * Retrieves products supplied by a specific vendor with stock quantities.
     */
    List<SupplierProductSummaryResponse> getSupplierProducts(Long supplierId);

    /**
     * Retrieves stock batch purchase history logs for a supplier.
     */
    List<SupplierPurchaseHistoryResponse> getSupplierPurchaseHistory(Long supplierId);

    /**
     * Updates supplier lifecycle status.
     */
    SupplierResponse updateSupplierStatus(Long id, SupplierStatus status);

    /**
     * Soft deletes specified supplier profile.
     */
    void deleteSupplier(Long id);
}
