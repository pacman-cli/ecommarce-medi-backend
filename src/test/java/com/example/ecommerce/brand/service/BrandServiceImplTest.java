package com.example.ecommerce.brand.service;

import com.example.ecommerce.brand.dto.request.BrandFilterRequest;
import com.example.ecommerce.brand.dto.request.BrandRequest;
import com.example.ecommerce.brand.dto.response.BrandResponse;
import com.example.ecommerce.brand.entity.Brand;
import com.example.ecommerce.brand.entity.BrandStatus;
import com.example.ecommerce.brand.mapper.BrandMapper;
import com.example.ecommerce.brand.repository.BrandRepository;
import com.example.ecommerce.brand.service.impl.BrandServiceImpl;
import com.example.ecommerce.brand.validator.BrandValidator;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
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
class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @Mock
    private BrandValidator brandValidator;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand;
    private BrandRequest brandRequest;
    private BrandResponse brandResponse;

    @BeforeEach
    void setUp() {
        brand = Brand.builder()
                .name("Apple")
                .slug("apple")
                .country("United States")
                .status(BrandStatus.ACTIVE)
                .sortOrder(1)
                .build();
        brand.setId(100L);

        brandRequest = BrandRequest.builder()
                .name("Apple")
                .country("United States")
                .websiteUrl("https://apple.com")
                .build();

        brandResponse = BrandResponse.builder()
                .id(100L)
                .name("Apple")
                .slug("apple")
                .country("United States")
                .status(BrandStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("createBrand should validate, auto-generate slug, and save brand")
    void createBrand_Success() {
        doNothing().when(brandValidator).validateForCreate(any());
        when(brandMapper.toEntity(any())).thenReturn(brand);
        when(brandRepository.save(any())).thenReturn(brand);
        when(brandMapper.toResponse(any())).thenReturn(brandResponse);

        BrandResponse result = brandService.createBrand(brandRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getName()).isEqualTo("Apple");

        verify(brandValidator).validateForCreate(brandRequest);
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    @DisplayName("getBrandBySlug should return brand when found")
    void getBrandBySlug_Success() {
        when(brandRepository.findBySlugAndDeletedFalse("apple")).thenReturn(Optional.of(brand));
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        BrandResponse response = brandService.getBrandBySlug("apple");

        assertThat(response).isNotNull();
        assertThat(response.getSlug()).isEqualTo("apple");
    }

    @Test
    @DisplayName("getBrandBySlug should throw ResourceNotFoundException when slug not found")
    void getBrandBySlug_NotFound() {
        when(brandRepository.findBySlugAndDeletedFalse("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> brandService.getBrandBySlug("unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Brand not found with slug: unknown");
    }

    @Test
    @DisplayName("deleteBrand should soft delete brand")
    void deleteBrand_Success() {
        when(brandRepository.findByIdAndDeletedFalse(100L)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any())).thenReturn(brand);

        brandService.deleteBrand(100L);

        assertThat(brand.isDeleted()).isTrue();
        assertThat(brand.getStatus()).isEqualTo(BrandStatus.INACTIVE);
        verify(brandRepository).save(brand);
    }

    @Test
    @DisplayName("getBrands should return paginated PageResponse")
    void getBrands_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Brand> brandPage = new PageImpl<>(List.of(brand));

        when(brandRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(brandPage);
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        BrandFilterRequest filter = BrandFilterRequest.builder().search("Apple").build();
        PageResponse<BrandResponse> pageResponse = brandService.getBrands(filter, pageable);

        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.getContent()).hasSize(1);
    }
}
