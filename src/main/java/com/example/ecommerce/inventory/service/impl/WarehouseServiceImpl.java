package com.example.ecommerce.inventory.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.inventory.dto.request.WarehouseRequest;
import com.example.ecommerce.inventory.dto.response.WarehouseResponse;
import com.example.ecommerce.inventory.entity.Warehouse;
import com.example.ecommerce.inventory.mapper.WarehouseMapper;
import com.example.ecommerce.inventory.repository.WarehouseRepository;
import com.example.ecommerce.inventory.service.WarehouseService;
import com.example.ecommerce.inventory.specification.WarehouseSpecification;
import com.example.ecommerce.inventory.validator.WarehouseValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final WarehouseValidator warehouseValidator;

    @Override
    @Transactional
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        log.info("Creating warehouse with code: {}", request.getCode());
        warehouseValidator.validateForCreate(request);

        Warehouse warehouse = warehouseMapper.toEntity(request);
        Warehouse saved = warehouseRepository.save(warehouse);
        log.info("Successfully created warehouse ID: {}", saved.getId());
        return warehouseMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public WarehouseResponse updateWarehouse(Long id, WarehouseRequest request) {
        log.info("Updating warehouse ID: {}", id);
        Warehouse warehouse = findEntityById(id);
        warehouseValidator.validateForUpdate(id, request);

        warehouseMapper.updateEntityFromRequest(request, warehouse);
        Warehouse updated = warehouseRepository.save(warehouse);
        log.info("Successfully updated warehouse ID: {}", updated.getId());
        return warehouseMapper.toResponse(updated);
    }

    @Override
    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = findEntityById(id);
        return warehouseMapper.toResponse(warehouse);
    }

    @Override
    public WarehouseResponse getWarehouseByCode(String code) {
        Warehouse warehouse = warehouseRepository.findByCodeAndDeletedFalse(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with code: " + code));
        return warehouseMapper.toResponse(warehouse);
    }

    @Override
    public PageResponse<WarehouseResponse> getWarehouses(String search, Boolean activeOnly, Pageable pageable) {
        Specification<Warehouse> spec = WarehouseSpecification.build(search, activeOnly);
        Page<Warehouse> page = warehouseRepository.findAll(spec, pageable);
        return PageResponse.from(page, warehouseMapper::toResponse);
    }

    @Override
    public List<WarehouseResponse> getAllActiveWarehouses() {
        List<Warehouse> list = warehouseRepository.findByActiveTrueAndDeletedFalse();
        return warehouseMapper.toResponseList(list);
    }

    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        log.info("Soft deleting warehouse ID: {}", id);
        Warehouse warehouse = findEntityById(id);
        warehouse.setDeleted(true);
        warehouse.setDeletedAt(Instant.now());
        warehouse.setActive(false);
        warehouseRepository.save(warehouse);
        log.info("Successfully soft deleted warehouse ID: {}", id);
    }

    private Warehouse findEntityById(Long id) {
        return warehouseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));
    }
}
