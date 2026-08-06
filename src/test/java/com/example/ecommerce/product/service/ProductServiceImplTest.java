package com.example.ecommerce.product.service;

import com.example.ecommerce.brand.entity.Brand;
import com.example.ecommerce.brand.repository.BrandRepository;
import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.category.repository.CategoryRepository;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.product.dto.request.ProductFilterRequest;
import com.example.ecommerce.product.dto.request.ProductRequest;
import com.example.ecommerce.product.dto.request.UpdateStockRequest;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.product.mapper.ProductMapper;
import com.example.ecommerce.product.repository.ProductImageRepository;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.product.service.impl.ProductServiceImpl;
import com.example.ecommerce.product.validator.ProductValidator;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private Brand brand;
    private Category category;

    @BeforeEach
    void setUp() {
        brand = Brand.builder().name("PharmaBrand").build();
        brand.setId(5L);

        category = Category.builder().name("Pain Relief").build();
        category.setId(12L);

        product = Product.builder()
                .name("Paracetamol 500mg Tablets")
                .slug("paracetamol-500mg-tablets")
                .sku("MED-PARA-500")
                .sellingPrice(new BigDecimal("5.99"))
                .quantity(100)
                .stockStatus(StockStatus.IN_STOCK)
                .status(ProductStatus.ACTIVE)
                .brand(brand)
                .category(category)
                .build();
        product.setId(200L);

        productRequest = ProductRequest.builder()
                .name("Paracetamol 500mg Tablets")
                .sku("MED-PARA-500")
                .sellingPrice(new BigDecimal("5.99"))
                .quantity(100)
                .brandId(5L)
                .categoryId(12L)
                .build();

        productResponse = ProductResponse.builder()
                .id(200L)
                .name("Paracetamol 500mg Tablets")
                .slug("paracetamol-500mg-tablets")
                .sku("MED-PARA-500")
                .sellingPrice(new BigDecimal("5.99"))
                .quantity(100)
                .stockStatus(StockStatus.IN_STOCK)
                .brandId(5L)
                .categoryId(12L)
                .build();
    }

    @Test
    @DisplayName("createProduct should validate, bind brand/category, generate slug and save product")
    void createProduct_Success() {
        doNothing().when(productValidator).validateForCreate(any());
        when(productMapper.toEntity(any())).thenReturn(product);
        when(brandRepository.findByIdAndDeletedFalse(5L)).thenReturn(Optional.of(brand));
        when(categoryRepository.findByIdAndDeletedFalse(12L)).thenReturn(Optional.of(category));
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toResponse(any())).thenReturn(productResponse);

        ProductResponse response = productService.createProduct(productRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(200L);
        assertThat(response.getSku()).isEqualTo("MED-PARA-500");

        verify(productValidator).validateForCreate(productRequest);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("getProductBySlug should return product response when found")
    void getProductBySlug_Success() {
        when(productRepository.findBySlugAndDeletedFalse("paracetamol-500mg-tablets")).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse response = productService.getProductBySlug("paracetamol-500mg-tablets");

        assertThat(response).isNotNull();
        assertThat(response.getSlug()).isEqualTo("paracetamol-500mg-tablets");
    }

    @Test
    @DisplayName("getProductBySlug should throw ResourceNotFoundException when not found")
    void getProductBySlug_NotFound() {
        when(productRepository.findBySlugAndDeletedFalse("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductBySlug("unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with slug: unknown");
    }

    @Test
    @DisplayName("updateStock should update stock levels and recalculate status")
    void updateStock_Success() {
        UpdateStockRequest stockRequest = UpdateStockRequest.builder()
                .quantity(0)
                .reservedQuantity(0)
                .build();

        when(productRepository.findByIdAndDeletedFalse(200L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        productService.updateStock(200L, stockRequest);

        assertThat(product.getQuantity()).isEqualTo(0);
        assertThat(product.getStockStatus()).isEqualTo(StockStatus.OUT_OF_STOCK);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("deleteProduct should soft delete product")
    void deleteProduct_Success() {
        when(productRepository.findByIdAndDeletedFalse(200L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        productService.deleteProduct(200L);

        assertThat(product.isDeleted()).isTrue();
        assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVE);
        assertThat(product.getStockStatus()).isEqualTo(StockStatus.OUT_OF_STOCK);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("getProducts should return paginated PageResponse")
    void getProducts_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductFilterRequest filter = ProductFilterRequest.builder().search("Paracetamol").build();
        PageResponse<ProductResponse> pageResponse = productService.getProducts(filter, pageable);

        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.getContent()).hasSize(1);
    }
}
