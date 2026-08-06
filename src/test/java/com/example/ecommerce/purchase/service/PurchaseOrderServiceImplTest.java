package com.example.ecommerce.purchase.service;

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
import com.example.ecommerce.purchase.dto.request.ReceivePurchaseItemsRequest;
import com.example.ecommerce.purchase.dto.request.RecordPurchasePaymentRequest;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderResponse;
import com.example.ecommerce.purchase.entity.PurchaseItem;
import com.example.ecommerce.purchase.entity.PurchaseOrder;
import com.example.ecommerce.purchase.mapper.PurchaseMapper;
import com.example.ecommerce.purchase.repository.PurchaseItemRepository;
import com.example.ecommerce.purchase.repository.PurchaseOrderRepository;
import com.example.ecommerce.purchase.service.impl.PurchaseOrderServiceImpl;
import com.example.ecommerce.purchase.validator.PurchaseOrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceImplTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private PurchaseItemRepository purchaseItemRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockBatchRepository stockBatchRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Spy
    private PurchaseOrderValidator purchaseOrderValidator;

    @InjectMocks
    private PurchaseOrderServiceImpl purchaseOrderService;

    private Supplier sampleSupplier;
    private Warehouse sampleWarehouse;
    private Product sampleProduct;
    private PurchaseOrder samplePO;
    private PurchaseItem sampleItem;
    private CreatePurchaseOrderRequest sampleCreateRequest;
    private PurchaseOrderResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleSupplier = Supplier.builder().name("Square Pharma").code("SUP-SQUARE").build();
        sampleSupplier.setId(10L);

        sampleWarehouse = Warehouse.builder().name("Central Warehouse").code("WH-CENTRAL").build();
        sampleWarehouse.setId(1L);

        sampleProduct = Product.builder().name("Napa 500mg").sku("MED-NAPA-500").sellingPrice(new BigDecimal("15.00")).build();
        sampleProduct.setId(200L);

        samplePO = PurchaseOrder.builder()
                .poNumber("PO-20260805-001001")
                .supplier(sampleSupplier)
                .warehouse(sampleWarehouse)
                .status(PurchaseStatus.DRAFT)
                .paymentStatus(PurchasePaymentStatus.UNPAID)
                .orderDate(LocalDate.now())
                .subtotal(new BigDecimal("1000.00"))
                .taxAmount(new BigDecimal("50.00"))
                .shippingCost(new BigDecimal("100.00"))
                .totalAmount(new BigDecimal("1150.00"))
                .paidAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();
        samplePO.setId(1001L);

        sampleItem = PurchaseItem.builder()
                .purchaseOrder(samplePO)
                .product(sampleProduct)
                .orderedQuantity(100)
                .receivedQuantity(0)
                .unitCost(new BigDecimal("10.00"))
                .totalCost(new BigDecimal("1000.00"))
                .build();
        sampleItem.setId(501L);
        samplePO.getItems().add(sampleItem);

        sampleCreateRequest = CreatePurchaseOrderRequest.builder()
                .supplierId(10L)
                .warehouseId(1L)
                .orderDate(LocalDate.now())
                .items(Collections.singletonList(
                        PurchaseItemRequest.builder()
                                .productId(200L)
                                .orderedQuantity(100)
                                .unitCost(new BigDecimal("10.00"))
                                .build()
                ))
                .build();

        sampleResponse = PurchaseOrderResponse.builder()
                .id(1001L)
                .poNumber("PO-20260805-001001")
                .status(PurchaseStatus.DRAFT)
                .paymentStatus(PurchasePaymentStatus.UNPAID)
                .totalAmount(new BigDecimal("1150.00"))
                .build();
    }

    @Test
    void testCreatePurchaseOrderSuccess() {
        when(supplierRepository.findByIdAndDeletedFalse(eq(10L))).thenReturn(Optional.of(sampleSupplier));
        when(warehouseRepository.findByIdAndDeletedFalse(eq(1L))).thenReturn(Optional.of(sampleWarehouse));
        when(productRepository.findById(eq(200L))).thenReturn(Optional.of(sampleProduct));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(samplePO);
        when(purchaseMapper.toResponse(any(PurchaseOrder.class))).thenReturn(sampleResponse);

        PurchaseOrderResponse response = purchaseOrderService.createPurchaseOrder(sampleCreateRequest);

        assertNotNull(response);
        assertEquals(1001L, response.getId());
        assertEquals(PurchaseStatus.DRAFT, response.getStatus());
        verify(purchaseOrderRepository, times(1)).save(any(PurchaseOrder.class));
    }

    @Test
    void testSubmitPurchaseOrderSuccess() {
        when(purchaseOrderRepository.findByIdAndDeletedFalse(eq(1001L))).thenReturn(Optional.of(samplePO));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(samplePO);
        when(purchaseMapper.toResponse(any(PurchaseOrder.class))).thenReturn(sampleResponse);

        PurchaseOrderResponse response = purchaseOrderService.submitPurchaseOrder(1001L);

        assertNotNull(response);
        assertEquals(PurchaseStatus.ORDERED, samplePO.getStatus());
        verify(purchaseOrderRepository, times(1)).save(eq(samplePO));
    }

    @Test
    void testReceivePurchaseItemsCreatesStockBatch() {
        samplePO.setStatus(PurchaseStatus.ORDERED);
        when(purchaseOrderRepository.findByIdAndDeletedFalse(eq(1001L))).thenReturn(Optional.of(samplePO));
        when(stockBatchRepository.findByBatchNumberAndProductIdAndWarehouseIdAndDeletedFalse(anyString(), eq(200L), eq(1L)))
                .thenReturn(Optional.empty());
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(samplePO);
        when(purchaseMapper.toResponse(any(PurchaseOrder.class))).thenReturn(sampleResponse);

        ReceivePurchaseItemsRequest request = ReceivePurchaseItemsRequest.builder()
                .items(Collections.singletonList(
                        ReceivePurchaseItemsRequest.ItemReceivingEntry.builder()
                                .itemId(501L)
                                .quantityReceived(100)
                                .batchNumber("LOT-20260805-A")
                                .build()
                ))
                .build();

        PurchaseOrderResponse response = purchaseOrderService.receivePurchaseItems(1001L, request);

        assertNotNull(response);
        assertEquals(100, sampleItem.getReceivedQuantity());
        assertEquals(PurchaseStatus.RECEIVED, samplePO.getStatus());
        verify(stockBatchRepository, times(1)).save(any(StockBatch.class));
    }

    @Test
    void testRecordPurchasePayment() {
        when(purchaseOrderRepository.findByIdAndDeletedFalse(eq(1001L))).thenReturn(Optional.of(samplePO));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(samplePO);
        when(purchaseMapper.toResponse(any(PurchaseOrder.class))).thenReturn(sampleResponse);

        RecordPurchasePaymentRequest request = RecordPurchasePaymentRequest.builder()
                .amount(new BigDecimal("1150.00"))
                .paymentReference("BANK-TRANSFER-99")
                .build();

        PurchaseOrderResponse response = purchaseOrderService.recordPurchasePayment(1001L, request);

        assertNotNull(response);
        assertEquals(new BigDecimal("1150.00"), samplePO.getPaidAmount());
        assertEquals(PurchasePaymentStatus.PAID, samplePO.getPaymentStatus());
    }
}
