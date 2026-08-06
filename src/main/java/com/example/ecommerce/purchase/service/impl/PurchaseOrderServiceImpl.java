package com.example.ecommerce.purchase.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.inventory.entity.BatchStatus;
import com.example.ecommerce.inventory.entity.StockBatch;
import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.inventory.entity.Warehouse;
import com.example.ecommerce.inventory.repository.StockBatchRepository;
import com.example.ecommerce.inventory.repository.SupplierRepository;
import com.example.ecommerce.inventory.repository.WarehouseRepository;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.purchase.dto.enums.PurchasePaymentStatus;
import com.example.ecommerce.purchase.dto.enums.PurchaseStatus;
import com.example.ecommerce.purchase.dto.request.CreatePurchaseOrderRequest;
import com.example.ecommerce.purchase.dto.request.PurchaseItemRequest;
import com.example.ecommerce.purchase.dto.request.PurchaseOrderFilterRequest;
import com.example.ecommerce.purchase.dto.request.ReceivePurchaseItemsRequest;
import com.example.ecommerce.purchase.dto.request.RecordPurchasePaymentRequest;
import com.example.ecommerce.purchase.dto.request.UpdatePurchaseOrderRequest;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderListResponse;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderResponse;
import com.example.ecommerce.purchase.entity.PurchaseItem;
import com.example.ecommerce.purchase.entity.PurchaseOrder;
import com.example.ecommerce.purchase.mapper.PurchaseMapper;
import com.example.ecommerce.purchase.repository.PurchaseItemRepository;
import com.example.ecommerce.purchase.repository.PurchaseOrderRepository;
import com.example.ecommerce.purchase.service.PurchaseOrderService;
import com.example.ecommerce.purchase.specification.PurchaseOrderSpecification;
import com.example.ecommerce.purchase.validator.PurchaseOrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service implementation managing purchase order lifecycles, procurement pricing,
 * goods receiving with automatic inventory stock batch creation, and payment tracking.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final StockBatchRepository stockBatchRepository;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseOrderValidator purchaseOrderValidator;

    @Override
    public PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request) {
        log.info("Creating purchase order for supplierId: {}, warehouseId: {}", request.getSupplierId(), request.getWarehouseId());

        Supplier supplier = supplierRepository.findByIdAndDeletedFalse(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId()));

        Warehouse warehouse = warehouseRepository.findByIdAndDeletedFalse(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        String poNumber = "PO-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        PurchaseOrder po = PurchaseOrder.builder()
                .poNumber(poNumber)
                .supplier(supplier)
                .warehouse(warehouse)
                .status(PurchaseStatus.DRAFT)
                .paymentStatus(PurchasePaymentStatus.UNPAID)
                .orderDate(request.getOrderDate())
                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                .invoiceNumber(request.getInvoiceNumber())
                .invoiceDate(request.getInvoiceDate())
                .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO)
                .shippingCost(request.getShippingCost() != null ? request.getShippingCost() : BigDecimal.ZERO)
                .paidAmount(BigDecimal.ZERO)
                .notes(request.getNotes())
                .items(new ArrayList<>())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;
        for (PurchaseItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemReq.getProductId()));

            BigDecimal lineTotal = itemReq.getUnitCost().multiply(BigDecimal.valueOf(itemReq.getOrderedQuantity()));
            subtotal = subtotal.add(lineTotal);

            PurchaseItem item = PurchaseItem.builder()
                    .product(product)
                    .orderedQuantity(itemReq.getOrderedQuantity())
                    .receivedQuantity(0)
                    .unitCost(itemReq.getUnitCost())
                    .totalCost(lineTotal)
                    .notes(itemReq.getNotes())
                    .build();

            po.addItem(item);
        }

        po.setSubtotal(subtotal);
        po.setTotalAmount(subtotal.add(po.getTaxAmount()).add(po.getShippingCost()));

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        log.info("Successfully created purchase order PO #: {} with ID: {}", saved.getPoNumber(), saved.getId());

        return purchaseMapper.toResponse(saved);
    }

    @Override
    public PurchaseOrderResponse updatePurchaseOrder(Long id, UpdatePurchaseOrderRequest request) {
        log.info("Updating purchase order ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        purchaseOrderValidator.validateModifiable(po);

        Supplier supplier = supplierRepository.findByIdAndDeletedFalse(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId()));

        Warehouse warehouse = warehouseRepository.findByIdAndDeletedFalse(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        po.setSupplier(supplier);
        po.setWarehouse(warehouse);
        po.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        po.setInvoiceNumber(request.getInvoiceNumber());
        po.setInvoiceDate(request.getInvoiceDate());
        po.setTaxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO);
        po.setShippingCost(request.getShippingCost() != null ? request.getShippingCost() : BigDecimal.ZERO);
        po.setNotes(request.getNotes());

        po.getItems().clear();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (PurchaseItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemReq.getProductId()));

            BigDecimal lineTotal = itemReq.getUnitCost().multiply(BigDecimal.valueOf(itemReq.getOrderedQuantity()));
            subtotal = subtotal.add(lineTotal);

            PurchaseItem item = PurchaseItem.builder()
                    .product(product)
                    .orderedQuantity(itemReq.getOrderedQuantity())
                    .receivedQuantity(0)
                    .unitCost(itemReq.getUnitCost())
                    .totalCost(lineTotal)
                    .notes(itemReq.getNotes())
                    .build();

            po.addItem(item);
        }

        po.setSubtotal(subtotal);
        po.setTotalAmount(subtotal.add(po.getTaxAmount()).add(po.getShippingCost()));

        PurchaseOrder updated = purchaseOrderRepository.save(po);
        return purchaseMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getPurchaseOrderById(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));
        return purchaseMapper.toResponse(po);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getPurchaseOrderByPoNumber(String poNumber) {
        PurchaseOrder po = purchaseOrderRepository.findByPoNumberAndDeletedFalse(poNumber)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "poNumber", poNumber));
        return purchaseMapper.toResponse(po);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseOrderListResponse> getPurchaseOrders(PurchaseOrderFilterRequest filter, Pageable pageable) {
        Page<PurchaseOrder> page = purchaseOrderRepository.findAll(PurchaseOrderSpecification.filterBy(filter), pageable);
        return PageResponse.from(page, purchaseMapper::toListResponse);
    }

    @Override
    public PurchaseOrderResponse submitPurchaseOrder(Long id) {
        log.info("Submitting purchase order ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        purchaseOrderValidator.validateSubmittable(po);
        po.setStatus(PurchaseStatus.ORDERED);

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        return purchaseMapper.toResponse(saved);
    }

    @Override
    public PurchaseOrderResponse receivePurchaseItems(Long id, ReceivePurchaseItemsRequest request) {
        log.info("Receiving items for purchase order ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        purchaseOrderValidator.validateReceivable(po);

        Map<Long, PurchaseItem> itemMap = po.getItems().stream()
                .collect(Collectors.toMap(PurchaseItem::getId, Function.identity()));

        for (ReceivePurchaseItemsRequest.ItemReceivingEntry entry : request.getItems()) {
            PurchaseItem item = itemMap.get(entry.getItemId());
            if (item == null) {
                throw new BadRequestException("Purchase item ID " + entry.getItemId() + " does not belong to PO #" + po.getPoNumber());
            }

            int currentReceived = item.getReceivedQuantity() != null ? item.getReceivedQuantity() : 0;
            int newTotalReceived = currentReceived + entry.getQuantityReceived();
            if (newTotalReceived > item.getOrderedQuantity()) {
                throw new BadRequestException(String.format("Received quantity (%d) for product %s exceeds remaining ordered quantity (%d)",
                        entry.getQuantityReceived(), item.getProduct().getName(), item.getOrderedQuantity() - currentReceived));
            }

            item.setReceivedQuantity(newTotalReceived);

            String batchNumber = StringUtils.hasText(entry.getBatchNumber())
                    ? entry.getBatchNumber()
                    : "LOT-" + System.currentTimeMillis() + "-" + item.getProduct().getId();

            Optional<StockBatch> existingBatchOpt = stockBatchRepository
                    .findByBatchNumberAndProductIdAndWarehouseIdAndDeletedFalse(batchNumber, item.getProduct().getId(), po.getWarehouse().getId());

            if (existingBatchOpt.isPresent()) {
                StockBatch batch = existingBatchOpt.get();
                batch.setQuantity(batch.getQuantity() + entry.getQuantityReceived());
                batch.setAvailableQuantity(batch.getAvailableQuantity() + entry.getQuantityReceived());
                stockBatchRepository.save(batch);
            } else {
                StockBatch batch = StockBatch.builder()
                        .product(item.getProduct())
                        .warehouse(po.getWarehouse())
                        .supplier(po.getSupplier())
                        .batchNumber(batchNumber)
                        .quantity(entry.getQuantityReceived())
                        .availableQuantity(entry.getQuantityReceived())
                        .reservedQuantity(0)
                        .purchasePrice(item.getUnitCost())
                        .sellingPrice(item.getProduct().getSellingPrice())
                        .expiryDate(entry.getExpiryDate())
                        .status(BatchStatus.AVAILABLE)
                        .build();
                stockBatchRepository.save(batch);
            }
        }

        boolean allFullyReceived = po.getItems().stream()
                .allMatch(i -> i.getReceivedQuantity() >= i.getOrderedQuantity());

        po.setStatus(allFullyReceived ? PurchaseStatus.RECEIVED : PurchaseStatus.PARTIALLY_RECEIVED);

        PurchaseOrder updated = purchaseOrderRepository.save(po);
        return purchaseMapper.toResponse(updated);
    }

    @Override
    public PurchaseOrderResponse recordPurchasePayment(Long id, RecordPurchasePaymentRequest request) {
        log.info("Recording payment of {} for purchase order ID: {}", request.getAmount(), id);
        PurchaseOrder po = purchaseOrderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        purchaseOrderValidator.validatePayment(po, request.getAmount());

        BigDecimal newPaidAmount = po.getPaidAmount().add(request.getAmount());
        po.setPaidAmount(newPaidAmount);

        if (newPaidAmount.compareTo(po.getTotalAmount()) >= 0) {
            po.setPaymentStatus(PurchasePaymentStatus.PAID);
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            po.setPaymentStatus(PurchasePaymentStatus.PARTIALLY_PAID);
        }

        PurchaseOrder updated = purchaseOrderRepository.save(po);
        return purchaseMapper.toResponse(updated);
    }

    @Override
    public PurchaseOrderResponse cancelPurchaseOrder(Long id) {
        log.info("Cancelling purchase order ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        if (po.getStatus() == PurchaseStatus.RECEIVED) {
            throw new BadRequestException("Cannot cancel a fully RECEIVED purchase order.");
        }

        po.setStatus(PurchaseStatus.CANCELLED);
        PurchaseOrder updated = purchaseOrderRepository.save(po);
        return purchaseMapper.toResponse(updated);
    }

    @Override
    public void deletePurchaseOrder(Long id) {
        log.info("Soft deleting purchase order ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", "id", id));

        po.setDeleted(true);
        po.setDeletedAt(Instant.now());
        purchaseOrderRepository.save(po);
    }
}
