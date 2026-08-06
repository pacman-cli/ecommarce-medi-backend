package com.example.ecommerce.inventory.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.inventory.dto.request.SupplierRequest;
import com.example.ecommerce.inventory.dto.response.SupplierResponse;
import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.inventory.mapper.SupplierMapper;
import com.example.ecommerce.inventory.repository.SupplierRepository;
import com.example.ecommerce.inventory.service.impl.SupplierServiceImpl;
import com.example.ecommerce.inventory.validator.SupplierValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @Mock
    private SupplierValidator supplierValidator;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier supplier;
    private SupplierRequest supplierRequest;
    private SupplierResponse supplierResponse;

    @BeforeEach
    void setUp() {
        supplier = Supplier.builder()
                .code("SUP-PHARMA-01")
                .name("Global PharmaCare Labs")
                .active(true)
                .build();
        supplier.setId(10L);

        supplierRequest = SupplierRequest.builder()
                .code("SUP-PHARMA-01")
                .name("Global PharmaCare Labs")
                .build();

        supplierResponse = SupplierResponse.builder()
                .id(10L)
                .code("SUP-PHARMA-01")
                .name("Global PharmaCare Labs")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("createSupplier should validate and save supplier")
    void createSupplier_Success() {
        doNothing().when(supplierValidator).validateForCreate(any());
        when(supplierMapper.toEntity(any())).thenReturn(supplier);
        when(supplierRepository.save(any())).thenReturn(supplier);
        when(supplierMapper.toResponse(any())).thenReturn(supplierResponse);

        SupplierResponse response = supplierService.createSupplier(supplierRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getCode()).isEqualTo("SUP-PHARMA-01");

        verify(supplierValidator).validateForCreate(supplierRequest);
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    @DisplayName("getSupplierByCode should return supplier when found")
    void getSupplierByCode_Success() {
        when(supplierRepository.findByCodeAndDeletedFalse("SUP-PHARMA-01")).thenReturn(Optional.of(supplier));
        when(supplierMapper.toResponse(supplier)).thenReturn(supplierResponse);

        SupplierResponse response = supplierService.getSupplierByCode("SUP-PHARMA-01");

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("SUP-PHARMA-01");
    }

    @Test
    @DisplayName("deleteSupplier should soft delete supplier")
    void deleteSupplier_Success() {
        when(supplierRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any())).thenReturn(supplier);

        supplierService.deleteSupplier(10L);

        assertThat(supplier.isDeleted()).isTrue();
        assertThat(supplier.isActive()).isFalse();
        verify(supplierRepository).save(supplier);
    }
}
