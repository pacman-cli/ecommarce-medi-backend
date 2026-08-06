package com.example.ecommerce.inventory.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.inventory.dto.request.WarehouseRequest;
import com.example.ecommerce.inventory.dto.response.WarehouseResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for warehouse management.
 */
public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseRequest request);

    WarehouseResponse updateWarehouse(Long id, WarehouseRequest request);

    WarehouseResponse getWarehouseById(Long id);

    WarehouseResponse getWarehouseByCode(String code);

    PageResponse<WarehouseResponse> getWarehouses(String search, Boolean activeOnly, Pageable pageable);

    List<WarehouseResponse> getAllActiveWarehouses();

    void deleteWarehouse(Long id);
}
