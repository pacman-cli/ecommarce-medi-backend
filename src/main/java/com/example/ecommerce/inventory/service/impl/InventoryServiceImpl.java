package com.example.ecommerce.inventory.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.inventory.dto.request.InventoryAdjustmentRequest;
import com.example.ecommerce.inventory.dto.request.InventoryFilterRequest;
import com.example.ecommerce.inventory.dto.request.StockBatchRequest;
import com.example.ecommerce.inventory.dto.response.InventoryAlertResponse;
import com.example.ecommerce.inventory.dto.response.InventoryTransactionResponse;
import com.example.ecommerce.inventory.dto.response.StockBatchResponse;
import com.example.ecommerce.inventory.entity.BatchStatus;
import com.example.ecommerce.inventory.entity.InventoryTransaction;
import com.example.ecommerce.inventory.entity.StockBatch;
import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.inventory.entity.TransactionType;
import com.example.ecommerce.inventory.entity.Warehouse;
import com.example.ecommerce.inventory.mapper.InventoryMapper;
import com.example.ecommerce.inventory.repository.InventoryTransactionRepository;
import com.example.ecommerce.inventory.repository.StockBatchRepository;
import com.example.ecommerce.inventory.repository.SupplierRepository;
import com.example.ecommerce.inventory.repository.WarehouseRepository;
import com.example.ecommerce.inventory.service.InventoryService;
import com.example.ecommerce.inventory.specification.InventorySpecification;
import com.example.ecommerce.inventory.validator.InventoryValidator;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation handling inbound purchase stock receipt, manual adjustments,
 * barcode/QR code lookup, transactional audit logging and inventory stock alerts.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final StockBatchRepository stockBatchRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryMapper inventoryMapper;
    private final InventoryValidator inventoryValidator;

    @Override
    @Transactional
    public StockBatchResponse receiveStockBatch(StockBatchRequest request) {
        log.info("Receiving stock batch: {} for product ID: {}", request.getBatchNumber(), request.getProductId());
        inventoryValidator.validateStockBatchForCreate(request);

        Product product = productRepository.findByIdAndDeletedFalse(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));

        Warehouse warehouse = warehouseRepository.findByIdAndDeletedFalse(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.getWarehouseId()));

        Supplier supplier = null;
        if (request.getSupplierId() != null) {
            supplier = supplierRepository.findByIdAndDeletedFalse(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + request.getSupplierId()));
        }

        StockBatch batch = inventoryMapper.toBatchEntity(request);
        batch.setProduct(product);
        batch.setWarehouse(warehouse);
        batch.setSupplier(supplier);

        // Auto-generate barcode & QR code if missing
        if (!StringUtils.hasText(batch.getBarcode())) {
            batch.setBarcode("BC-" + request.getProductId() + "-" + System.currentTimeMillis());
        }
        if (!StringUtils.hasText(batch.getQrCode())) {
            batch.setQrCode("https://inventory.example.com/qr/batch/" + batch.getBatchNumber());
        }

        batch.recalculateAvailable();
        StockBatch savedBatch = stockBatchRepository.save(batch);

        // Record Audit Transaction
        BigDecimal unitPrice = request.getPurchasePrice() != null ? request.getPurchasePrice() : product.getCostPrice();
        BigDecimal totalVal = unitPrice != null ? unitPrice.multiply(BigDecimal.valueOf(request.getQuantity())) : BigDecimal.ZERO;

        InventoryTransaction tx = InventoryTransaction.builder()
                .product(product)
                .stockBatch(savedBatch)
                .warehouse(warehouse)
                .supplier(supplier)
                .transactionType(TransactionType.INBOUND_PURCHASE)
                .quantity(request.getQuantity())
                .unitPrice(unitPrice)
                .totalValue(totalVal)
                .referenceNumber("PO-RECEIPT-" + savedBatch.getBatchNumber())
                .reason("Inbound purchase stock receipt")
                .performedBy(getCurrentUser())
                .transactionDate(Instant.now())
                .build();
        transactionRepository.save(tx);

        // Sync Product total stock
        syncProductStock(product);

        log.info("Successfully received stock batch ID: {}", savedBatch.getId());
        return inventoryMapper.toBatchResponse(savedBatch);
    }

    @Override
    @Transactional
    public StockBatchResponse adjustStock(InventoryAdjustmentRequest request) {
        log.info("Adjusting stock for product ID: {}, type: {}", request.getProductId(), request.getTransactionType());
        Product product = productRepository.findByIdAndDeletedFalse(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));

        StockBatch batch = null;
        if (request.getStockBatchId() != null) {
            batch = stockBatchRepository.findByIdAndDeletedFalse(request.getStockBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Stock batch not found with ID: " + request.getStockBatchId()));
        }

        inventoryValidator.validateAdjustment(request, batch);

        int signedQty = request.getQuantity();
        if (request.getTransactionType() == TransactionType.ADJUSTMENT_DECREASE
                || request.getTransactionType() == TransactionType.OUTBOUND_SALE
                || request.getTransactionType() == TransactionType.EXPIRED_DISCARD
                || request.getTransactionType() == TransactionType.RETURN_SUPPLIER) {
            signedQty = -request.getQuantity();
        }

        Warehouse warehouse = batch != null ? batch.getWarehouse() : null;
        if (warehouse == null && request.getWarehouseId() != null) {
            warehouse = warehouseRepository.findByIdAndDeletedFalse(request.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.getWarehouseId()));
        }

        Supplier supplier = batch != null ? batch.getSupplier() : null;
        if (supplier == null && request.getSupplierId() != null) {
            supplier = supplierRepository.findByIdAndDeletedFalse(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + request.getSupplierId()));
        }

        if (batch != null) {
            batch.setQuantity(Math.max(0, batch.getQuantity() + signedQty));
            batch.recalculateAvailable();
            stockBatchRepository.save(batch);
        }

        BigDecimal unitPrice = request.getUnitPrice() != null ? request.getUnitPrice() : (batch != null && batch.getSellingPrice() != null ? batch.getSellingPrice() : product.getSellingPrice());
        BigDecimal totalVal = unitPrice != null ? unitPrice.multiply(BigDecimal.valueOf(Math.abs(signedQty))) : BigDecimal.ZERO;

        InventoryTransaction tx = InventoryTransaction.builder()
                .product(product)
                .stockBatch(batch)
                .warehouse(warehouse)
                .supplier(supplier)
                .transactionType(request.getTransactionType())
                .quantity(signedQty)
                .unitPrice(unitPrice)
                .totalValue(totalVal)
                .referenceNumber(request.getReferenceNumber())
                .reason(request.getReason())
                .performedBy(getCurrentUser())
                .transactionDate(Instant.now())
                .build();
        transactionRepository.save(tx);

        syncProductStock(product);

        return batch != null ? inventoryMapper.toBatchResponse(batch) : null;
    }

    @Override
    public StockBatchResponse getBatchById(Long id) {
        StockBatch batch = stockBatchRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock batch not found with ID: " + id));
        return inventoryMapper.toBatchResponse(batch);
    }

    @Override
    public StockBatchResponse getBatchByBarcode(String barcode) {
        StockBatch batch = stockBatchRepository.findByBarcodeAndDeletedFalse(barcode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Stock batch not found with barcode: " + barcode));
        return inventoryMapper.toBatchResponse(batch);
    }

    @Override
    public StockBatchResponse getBatchByQrCode(String qrCode) {
        StockBatch batch = stockBatchRepository.findByQrCodeAndDeletedFalse(qrCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Stock batch not found with QR code: " + qrCode));
        return inventoryMapper.toBatchResponse(batch);
    }

    @Override
    public PageResponse<StockBatchResponse> getStockBatches(InventoryFilterRequest filter, Pageable pageable) {
        Specification<StockBatch> spec = InventorySpecification.buildBatchSpec(filter);
        Page<StockBatch> page = stockBatchRepository.findAll(spec, pageable);
        return PageResponse.from(page, inventoryMapper::toBatchResponse);
    }

    @Override
    public PageResponse<InventoryTransactionResponse> getTransactionHistory(InventoryFilterRequest filter, Pageable pageable) {
        Specification<InventoryTransaction> spec = InventorySpecification.buildTransactionSpec(filter);
        Page<InventoryTransaction> page = transactionRepository.findAll(spec, pageable);
        return PageResponse.from(page, inventoryMapper::toTransactionResponse);
    }

    @Override
    public List<InventoryAlertResponse> getLowStockAlerts() {
        List<StockBatch> lowBatches = stockBatchRepository.findByStatusAndDeletedFalse(BatchStatus.LOW_STOCK);
        Map<Product, List<StockBatch>> grouped = lowBatches.stream().collect(Collectors.groupingBy(StockBatch::getProduct));

        List<InventoryAlertResponse> alerts = new ArrayList<>();
        grouped.forEach((product, batches) -> {
            int total = batches.stream().mapToInt(StockBatch::getAvailableQuantity).sum();
            alerts.add(InventoryAlertResponse.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .productSku(product.getSku())
                    .totalStock(total)
                    .alertType("LOW_STOCK")
                    .batches(inventoryMapper.toBatchResponseList(batches))
                    .build());
        });
        return alerts;
    }

    @Override
    public List<InventoryAlertResponse> getOutOfStockAlerts() {
        List<Product> products = productRepository.findAll();
        List<InventoryAlertResponse> alerts = new ArrayList<>();
        for (Product p : products) {
            if (!p.isDeleted() && (p.getQuantity() == null || p.getQuantity() <= 0)) {
                alerts.add(InventoryAlertResponse.builder()
                        .productId(p.getId())
                        .productName(p.getName())
                        .productSku(p.getSku())
                        .totalStock(0)
                        .alertType("OUT_OF_STOCK")
                        .batches(new ArrayList<>())
                        .build());
            }
        }
        return alerts;
    }

    @Override
    public List<StockBatchResponse> getExpiredBatches() {
        List<StockBatch> expired = stockBatchRepository.findByExpiryDateBeforeAndDeletedFalse(LocalDate.now());
        return inventoryMapper.toBatchResponseList(expired);
    }

    private void syncProductStock(Product product) {
        Integer totalAvailable = stockBatchRepository.sumAvailableQuantityByProductId(product.getId());
        product.setQuantity(totalAvailable != null ? totalAvailable : 0);
        product.recalculateStockStatus();
        productRepository.save(product);
    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "system";
    }
}
