package com.example.ecommerce.inventory.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.inventory.dto.request.SupplierRequest;
import com.example.ecommerce.inventory.dto.response.SupplierResponse;
import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.inventory.mapper.SupplierMapper;
import com.example.ecommerce.inventory.repository.SupplierRepository;
import com.example.ecommerce.inventory.service.SupplierService;
import com.example.ecommerce.inventory.specification.SupplierSpecification;
import com.example.ecommerce.inventory.validator.SupplierValidator;
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
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierValidator supplierValidator;

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        log.info("Creating supplier with code: {}", request.getCode());
        supplierValidator.validateForCreate(request);

        Supplier supplier = supplierMapper.toEntity(request);
        Supplier saved = supplierRepository.save(supplier);
        log.info("Successfully created supplier ID: {}", saved.getId());
        return supplierMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier ID: {}", id);
        Supplier supplier = findEntityById(id);
        supplierValidator.validateForUpdate(id, request);

        supplierMapper.updateEntityFromRequest(request, supplier);
        Supplier updated = supplierRepository.save(supplier);
        log.info("Successfully updated supplier ID: {}", updated.getId());
        return supplierMapper.toResponse(updated);
    }

    @Override
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = findEntityById(id);
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public SupplierResponse getSupplierByCode(String code) {
        Supplier supplier = supplierRepository.findByCodeAndDeletedFalse(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + code));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public PageResponse<SupplierResponse> getSuppliers(String search, Boolean activeOnly, Pageable pageable) {
        Specification<Supplier> spec = SupplierSpecification.build(search, activeOnly);
        Page<Supplier> page = supplierRepository.findAll(spec, pageable);
        return PageResponse.from(page, supplierMapper::toResponse);
    }

    @Override
    public List<SupplierResponse> getAllActiveSuppliers() {
        List<Supplier> list = supplierRepository.findByActiveTrueAndDeletedFalse();
        return supplierMapper.toResponseList(list);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        log.info("Soft deleting supplier ID: {}", id);
        Supplier supplier = findEntityById(id);
        supplier.setDeleted(true);
        supplier.setDeletedAt(Instant.now());
        supplier.setActive(false);
        supplierRepository.save(supplier);
        log.info("Successfully soft deleted supplier ID: {}", id);
    }

    private Supplier findEntityById(Long id) {
        return supplierRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
    }
}
