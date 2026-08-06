package com.example.ecommerce.supplier.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.inventory.entity.StockBatch;
import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.inventory.repository.StockBatchRepository;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.supplier.dto.enums.SupplierStatus;
import com.example.ecommerce.supplier.dto.request.SupplierFilterRequest;
import com.example.ecommerce.supplier.dto.request.SupplierRequest;
import com.example.ecommerce.supplier.dto.response.SupplierDetailResponse;
import com.example.ecommerce.supplier.dto.response.SupplierProductSummaryResponse;
import com.example.ecommerce.supplier.dto.response.SupplierPurchaseHistoryResponse;
import com.example.ecommerce.supplier.dto.response.SupplierResponse;
import com.example.ecommerce.supplier.mapper.VendorSupplierMapper;
import com.example.ecommerce.supplier.repository.SupplierRepository;
import com.example.ecommerce.supplier.service.SupplierService;
import com.example.ecommerce.supplier.specification.SupplierSpecification;
import com.example.ecommerce.supplier.validator.SupplierValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SupplierService} managing supplier profiles, trade licenses,
 * TIN credentials, status transitions, product catalog lookups, and purchase history.
 */
@Slf4j
@Service("supplierModuleService")
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final StockBatchRepository stockBatchRepository;
    private final VendorSupplierMapper supplierMapper;
    private final SupplierValidator supplierValidator;

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        log.info("Creating supplier with code: {}, company: {}", request.getCode(), request.getName());
        supplierValidator.validateCreate(request);

        Supplier supplier = supplierMapper.toEntity(request);
        Supplier saved = supplierRepository.save(supplier);

        log.info("Successfully created supplier ID: {}", saved.getId());
        return supplierMapper.toResponse(saved);
    }

    @Override
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier ID: {}", id);
        supplierValidator.validateUpdate(id, request);

        Supplier supplier = supplierRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));

        supplierMapper.updateEntityFromRequest(request, supplier);
        Supplier updated = supplierRepository.save(supplier);

        log.info("Successfully updated supplier ID: {}", updated.getId());
        return supplierMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierDetailResponse getSupplierDetailById(Long id) {
        Supplier supplier = supplierRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));

        SupplierResponse profile = supplierMapper.toResponse(supplier);
        List<SupplierProductSummaryResponse> products = getSupplierProducts(id);
        List<SupplierPurchaseHistoryResponse> history = getSupplierPurchaseHistory(id);

        BigDecimal totalExpenditure = history.stream()
                .map(SupplierPurchaseHistoryResponse::getTotalBatchCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return SupplierDetailResponse.builder()
                .profile(profile)
                .totalProductsSupplied(products.size())
                .totalBatchesReceived(history.size())
                .totalPurchaseExpenditure(totalExpenditure)
                .products(products)
                .purchaseHistory(history)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SupplierResponse> getSuppliers(SupplierFilterRequest filter, Pageable pageable) {
        Page<Supplier> page = supplierRepository.findAll(SupplierSpecification.filterBy(filter), pageable);
        return PageResponse.from(page, supplierMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllActiveSuppliers() {
        List<Supplier> suppliers = supplierRepository.findByActiveTrueAndDeletedFalse();
        return supplierMapper.toResponseList(suppliers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierProductSummaryResponse> getSupplierProducts(Long supplierId) {
        List<StockBatch> batches = stockBatchRepository.findBySupplierIdAndDeletedFalse(supplierId);

        Map<Long, List<StockBatch>> batchesByProduct = batches.stream()
                .filter(b -> b.getProduct() != null)
                .collect(Collectors.groupingBy(b -> b.getProduct().getId()));

        List<SupplierProductSummaryResponse> list = new ArrayList<>();
        for (Map.Entry<Long, List<StockBatch>> entry : batchesByProduct.entrySet()) {
            List<StockBatch> productBatches = entry.getValue();
            Product p = productBatches.get(0).getProduct();

            int totalQty = productBatches.stream()
                    .mapToInt(b -> b.getAvailableQuantity() != null ? b.getAvailableQuantity() : 0)
                    .sum();

            SupplierProductSummaryResponse item = SupplierProductSummaryResponse.builder()
                    .productId(p.getId())
                    .productName(p.getName())
                    .sku(p.getSku())
                    .brandName(p.getBrand() != null ? p.getBrand().getName() : null)
                    .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                    .price(p.getSellingPrice())
                    .totalQuantityInStock(totalQty)
                    .totalBatchesCount(productBatches.size())
                    .build();
            list.add(item);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierPurchaseHistoryResponse> getSupplierPurchaseHistory(Long supplierId) {
        List<StockBatch> batches = stockBatchRepository.findBySupplierIdAndDeletedFalse(supplierId);

        return batches.stream().map(b -> {
            BigDecimal unitPrice = b.getPurchasePrice() != null ? b.getPurchasePrice() : BigDecimal.ZERO;
            int initialQty = b.getQuantity() != null ? b.getQuantity() : 0;
            BigDecimal totalCost = unitPrice.multiply(BigDecimal.valueOf(initialQty));

            return SupplierPurchaseHistoryResponse.builder()
                    .batchId(b.getId())
                    .lotNumber(b.getBatchNumber())
                    .productId(b.getProduct() != null ? b.getProduct().getId() : null)
                    .productName(b.getProduct() != null ? b.getProduct().getName() : null)
                    .warehouseId(b.getWarehouse() != null ? b.getWarehouse().getId() : null)
                    .warehouseName(b.getWarehouse() != null ? b.getWarehouse().getName() : null)
                    .initialQuantity(initialQty)
                    .currentQuantity(b.getAvailableQuantity() != null ? b.getAvailableQuantity() : 0)
                    .purchasePrice(unitPrice)
                    .totalBatchCost(totalCost)
                    .expirationDate(b.getExpiryDate())
                    .receivedAt(b.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public SupplierResponse updateSupplierStatus(Long id, SupplierStatus status) {
        log.info("Updating status for supplier ID: {} to {}", id, status);
        Supplier supplier = supplierRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));

        supplier.setStatus(status);
        supplier.setActive(status == SupplierStatus.ACTIVE);

        Supplier updated = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updated);
    }

    @Override
    public void deleteSupplier(Long id) {
        log.info("Soft deleting supplier ID: {}", id);
        Supplier supplier = supplierRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));

        supplier.setDeleted(true);
        supplier.setDeletedAt(Instant.now());
        supplierRepository.save(supplier);

        log.info("Successfully soft deleted supplier ID: {}", id);
    }
}
