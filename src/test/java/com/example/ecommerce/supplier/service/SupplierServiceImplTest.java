package com.example.ecommerce.supplier.service;

import com.example.ecommerce.inventory.entity.StockBatch;
import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.inventory.repository.StockBatchRepository;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.supplier.dto.enums.SupplierStatus;
import com.example.ecommerce.supplier.dto.request.SupplierRequest;
import com.example.ecommerce.supplier.dto.response.SupplierDetailResponse;
import com.example.ecommerce.supplier.dto.response.SupplierProductSummaryResponse;
import com.example.ecommerce.supplier.dto.response.SupplierPurchaseHistoryResponse;
import com.example.ecommerce.supplier.dto.response.SupplierResponse;
import com.example.ecommerce.supplier.mapper.VendorSupplierMapper;
import com.example.ecommerce.supplier.repository.SupplierRepository;
import com.example.ecommerce.supplier.service.impl.SupplierServiceImpl;
import com.example.ecommerce.supplier.validator.SupplierValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private StockBatchRepository stockBatchRepository;

    @Mock
    private VendorSupplierMapper supplierMapper;

    @Mock
    private SupplierValidator supplierValidator;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier sampleSupplier;
    private SupplierRequest sampleRequest;
    private SupplierResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleSupplier = Supplier.builder()
                .code("SUP-PHARMA-01")
                .name("Square Pharmaceuticals PLC")
                .contactPerson("Dr. Rafiqul Islam")
                .email("info@squarepharma.com.bd")
                .phone("+88028833047")
                .tradeLicense("TL-DHAKA-2026")
                .tin("TIN-1234567890")
                .status(SupplierStatus.ACTIVE)
                .active(true)
                .build();
        sampleSupplier.setId(10L);

        sampleRequest = SupplierRequest.builder()
                .code("SUP-PHARMA-01")
                .name("Square Pharmaceuticals PLC")
                .contactPerson("Dr. Rafiqul Islam")
                .email("info@squarepharma.com.bd")
                .phone("+88028833047")
                .tradeLicense("TL-DHAKA-2026")
                .tin("TIN-1234567890")
                .status(SupplierStatus.ACTIVE)
                .build();

        sampleResponse = SupplierResponse.builder()
                .id(10L)
                .code("SUP-PHARMA-01")
                .name("Square Pharmaceuticals PLC")
                .companyName("Square Pharmaceuticals PLC")
                .tradeLicense("TL-DHAKA-2026")
                .tin("TIN-1234567890")
                .status(SupplierStatus.ACTIVE)
                .active(true)
                .build();
    }

    @Test
    void testCreateSupplierSuccess() {
        doNothing().when(supplierValidator).validateCreate(any(SupplierRequest.class));
        when(supplierMapper.toEntity(any(SupplierRequest.class))).thenReturn(sampleSupplier);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(sampleSupplier);
        when(supplierMapper.toResponse(any(Supplier.class))).thenReturn(sampleResponse);

        SupplierResponse response = supplierService.createSupplier(sampleRequest);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Square Pharmaceuticals PLC", response.getCompanyName());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testGetSupplierDetailById() {
        Product sampleProduct = Product.builder().name("Napa 500mg").sku("MED-NAPA").sellingPrice(new BigDecimal("15.00")).build();
        sampleProduct.setId(200L);

        StockBatch sampleBatch = StockBatch.builder()
                .batchNumber("LOT-20260804-A")
                .product(sampleProduct)
                .supplier(sampleSupplier)
                .quantity(500)
                .availableQuantity(450)
                .purchasePrice(new BigDecimal("10.00"))
                .build();
        sampleBatch.setId(50L);

        when(supplierRepository.findByIdAndDeletedFalse(eq(10L))).thenReturn(Optional.of(sampleSupplier));
        when(supplierMapper.toResponse(eq(sampleSupplier))).thenReturn(sampleResponse);
        when(stockBatchRepository.findBySupplierIdAndDeletedFalse(eq(10L))).thenReturn(Collections.singletonList(sampleBatch));

        SupplierDetailResponse response = supplierService.getSupplierDetailById(10L);

        assertNotNull(response);
        assertEquals(10L, response.getProfile().getId());
        assertEquals(1, response.getTotalProductsSupplied());
        assertEquals(1, response.getTotalBatchesReceived());
        assertEquals(new BigDecimal("5000.00"), response.getTotalPurchaseExpenditure());
    }

    @Test
    void testUpdateSupplierStatus() {
        when(supplierRepository.findByIdAndDeletedFalse(eq(10L))).thenReturn(Optional.of(sampleSupplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(sampleSupplier);
        when(supplierMapper.toResponse(any(Supplier.class))).thenReturn(sampleResponse);

        SupplierResponse response = supplierService.updateSupplierStatus(10L, SupplierStatus.SUSPENDED);

        assertNotNull(response);
        assertEquals(SupplierStatus.SUSPENDED, sampleSupplier.getStatus());
        assertFalse(sampleSupplier.isActive());
    }

    @Test
    void testDeleteSupplierSuccess() {
        when(supplierRepository.findByIdAndDeletedFalse(eq(10L))).thenReturn(Optional.of(sampleSupplier));

        supplierService.deleteSupplier(10L);

        assertTrue(sampleSupplier.isDeleted());
        assertNotNull(sampleSupplier.getDeletedAt());
        verify(supplierRepository, times(1)).save(eq(sampleSupplier));
    }
}
