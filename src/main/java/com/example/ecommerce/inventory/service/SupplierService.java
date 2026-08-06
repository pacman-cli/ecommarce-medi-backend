package com.example.ecommerce.inventory.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.inventory.dto.request.SupplierRequest;
import com.example.ecommerce.inventory.dto.response.SupplierResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for supplier vendor management.
 */
public interface SupplierService {

    SupplierResponse createSupplier(SupplierRequest request);

    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    SupplierResponse getSupplierById(Long id);

    SupplierResponse getSupplierByCode(String code);

    PageResponse<SupplierResponse> getSuppliers(String search, Boolean activeOnly, Pageable pageable);

    List<SupplierResponse> getAllActiveSuppliers();

    void deleteSupplier(Long id);
}
