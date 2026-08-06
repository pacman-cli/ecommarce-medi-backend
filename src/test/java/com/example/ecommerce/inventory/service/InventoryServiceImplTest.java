package com.example.ecommerce.inventory.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.inventory.dto.request.InventoryAdjustmentRequest;
import com.example.ecommerce.inventory.dto.request.StockBatchRequest;
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
import com.example.ecommerce.inventory.service.impl.InventoryServiceImpl;
import com.example.ecommerce.inventory.validator.InventoryValidator;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private StockBatchRepository stockBatchRepository;

    @Mock
    private InventoryTransactionRepository transactionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @Mock
    private InventoryValidator inventoryValidator;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Product product;
    private Warehouse warehouse;
    private Supplier supplier;
    private StockBatch stockBatch;
    private StockBatchRequest stockBatchRequest;
    private StockBatchResponse stockBatchResponse;

    @BeforeEach
    void setUp() {
        product = Product.builder().name("Paracetamol 500mg").sku("MED-PARA-500").sellingPrice(new BigDecimal("5.99")).quantity(500).build();
        product.setId(200L);

        warehouse = Warehouse.builder().code("WH-CENTRAL-01").name("Central Hub").build();
        warehouse.setId(1L);

        supplier = Supplier.builder().code("SUP-PHARMA-01").name("PharmaCare").build();
        supplier.setId(10L);

        stockBatch = StockBatch.builder()
                .product(product)
                .warehouse(warehouse)
                .supplier(supplier)
                .batchNumber("LOT-20260804-A")
                .quantity(500)
                .availableQuantity(500)
                .status(BatchStatus.AVAILABLE)
                .build();
        stockBatch.setId(50L);

        stockBatchRequest = StockBatchRequest.builder()
                .productId(200L)
                .warehouseId(1L)
                .supplierId(10L)
                .batchNumber("LOT-20260804-A")
                .quantity(500)
                .build();

        stockBatchResponse = StockBatchResponse.builder()
                .id(50L)
                .productId(200L)
                .warehouseId(1L)
                .supplierId(10L)
                .batchNumber("LOT-20260804-A")
                .quantity(500)
                .availableQuantity(500)
                .status(BatchStatus.AVAILABLE)
                .build();
    }

    @Test
    @DisplayName("receiveStockBatch should validate, save batch, log transaction, and sync product stock")
    void receiveStockBatch_Success() {
        doNothing().when(inventoryValidator).validateStockBatchForCreate(any());
        when(productRepository.findByIdAndDeletedFalse(200L)).thenReturn(Optional.of(product));
        when(warehouseRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(warehouse));
        when(supplierRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(supplier));
        when(inventoryMapper.toBatchEntity(any())).thenReturn(stockBatch);
        when(stockBatchRepository.save(any())).thenReturn(stockBatch);
        when(transactionRepository.save(any())).thenReturn(new InventoryTransaction());
        when(stockBatchRepository.sumAvailableQuantityByProductId(200L)).thenReturn(500);
        when(productRepository.save(any())).thenReturn(product);
        when(inventoryMapper.toBatchResponse(any())).thenReturn(stockBatchResponse);

        StockBatchResponse response = inventoryService.receiveStockBatch(stockBatchRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(50L);
        assertThat(response.getBatchNumber()).isEqualTo("LOT-20260804-A");

        verify(inventoryValidator).validateStockBatchForCreate(stockBatchRequest);
        verify(stockBatchRepository).save(any(StockBatch.class));
        verify(transactionRepository).save(any(InventoryTransaction.class));
    }

    @Test
    @DisplayName("getBatchByBarcode should return batch when found")
    void getBatchByBarcode_Success() {
        when(stockBatchRepository.findByBarcodeAndDeletedFalse("8901122334455")).thenReturn(Optional.of(stockBatch));
        when(inventoryMapper.toBatchResponse(stockBatch)).thenReturn(stockBatchResponse);

        StockBatchResponse response = inventoryService.getBatchByBarcode("8901122334455");

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("getBatchByBarcode should throw ResourceNotFoundException when barcode not found")
    void getBatchByBarcode_NotFound() {
        when(stockBatchRepository.findByBarcodeAndDeletedFalse("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.getBatchByBarcode("UNKNOWN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Stock batch not found with barcode: UNKNOWN");
    }

    @Test
    @DisplayName("adjustStock should update batch stock, log transaction and sync product")
    void adjustStock_Success() {
        InventoryAdjustmentRequest adjustReq = InventoryAdjustmentRequest.builder()
                .productId(200L)
                .stockBatchId(50L)
                .transactionType(TransactionType.ADJUSTMENT_DECREASE)
                .quantity(50)
                .reason("Damaged stock discard")
                .build();

        when(productRepository.findByIdAndDeletedFalse(200L)).thenReturn(Optional.of(product));
        when(stockBatchRepository.findByIdAndDeletedFalse(50L)).thenReturn(Optional.of(stockBatch));
        doNothing().when(inventoryValidator).validateAdjustment(any(), any());
        when(stockBatchRepository.save(any())).thenReturn(stockBatch);
        when(transactionRepository.save(any())).thenReturn(new InventoryTransaction());
        when(stockBatchRepository.sumAvailableQuantityByProductId(200L)).thenReturn(450);
        when(productRepository.save(any())).thenReturn(product);
        when(inventoryMapper.toBatchResponse(any())).thenReturn(stockBatchResponse);

        inventoryService.adjustStock(adjustReq);

        verify(stockBatchRepository).save(stockBatch);
        verify(transactionRepository).save(any(InventoryTransaction.class));
    }
}
