package com.example.ecommerce.inventory.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.inventory.dto.request.WarehouseRequest;
import com.example.ecommerce.inventory.dto.response.WarehouseResponse;
import com.example.ecommerce.inventory.entity.Warehouse;
import com.example.ecommerce.inventory.mapper.WarehouseMapper;
import com.example.ecommerce.inventory.repository.WarehouseRepository;
import com.example.ecommerce.inventory.service.impl.WarehouseServiceImpl;
import com.example.ecommerce.inventory.validator.WarehouseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    @Mock
    private WarehouseValidator warehouseValidator;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Warehouse warehouse;
    private WarehouseRequest warehouseRequest;
    private WarehouseResponse warehouseResponse;

    @BeforeEach
    void setUp() {
        warehouse = Warehouse.builder()
                .code("WH-CENTRAL-01")
                .name("Central Distribution Hub")
                .active(true)
                .build();
        warehouse.setId(1L);

        warehouseRequest = WarehouseRequest.builder()
                .code("WH-CENTRAL-01")
                .name("Central Distribution Hub")
                .build();

        warehouseResponse = WarehouseResponse.builder()
                .id(1L)
                .code("WH-CENTRAL-01")
                .name("Central Distribution Hub")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("createWarehouse should validate and save warehouse")
    void createWarehouse_Success() {
        doNothing().when(warehouseValidator).validateForCreate(any());
        when(warehouseMapper.toEntity(any())).thenReturn(warehouse);
        when(warehouseRepository.save(any())).thenReturn(warehouse);
        when(warehouseMapper.toResponse(any())).thenReturn(warehouseResponse);

        WarehouseResponse response = warehouseService.createWarehouse(warehouseRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCode()).isEqualTo("WH-CENTRAL-01");

        verify(warehouseValidator).validateForCreate(warehouseRequest);
        verify(warehouseRepository).save(any(Warehouse.class));
    }

    @Test
    @DisplayName("getWarehouseByCode should return warehouse when found")
    void getWarehouseByCode_Success() {
        when(warehouseRepository.findByCodeAndDeletedFalse("WH-CENTRAL-01")).thenReturn(Optional.of(warehouse));
        when(warehouseMapper.toResponse(warehouse)).thenReturn(warehouseResponse);

        WarehouseResponse response = warehouseService.getWarehouseByCode("WH-CENTRAL-01");

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("WH-CENTRAL-01");
    }

    @Test
    @DisplayName("getWarehouseByCode should throw ResourceNotFoundException when not found")
    void getWarehouseByCode_NotFound() {
        when(warehouseRepository.findByCodeAndDeletedFalse("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> warehouseService.getWarehouseByCode("UNKNOWN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Warehouse not found with code: UNKNOWN");
    }

    @Test
    @DisplayName("deleteWarehouse should soft delete warehouse")
    void deleteWarehouse_Success() {
        when(warehouseRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.save(any())).thenReturn(warehouse);

        warehouseService.deleteWarehouse(1L);

        assertThat(warehouse.isDeleted()).isTrue();
        assertThat(warehouse.isActive()).isFalse();
        verify(warehouseRepository).save(warehouse);
    }
}
