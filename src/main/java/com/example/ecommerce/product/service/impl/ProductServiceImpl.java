package com.example.ecommerce.product.service.impl;

import com.example.ecommerce.brand.entity.Brand;
import com.example.ecommerce.brand.repository.BrandRepository;
import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.category.repository.CategoryRepository;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.product.dto.request.ProductFilterRequest;
import com.example.ecommerce.product.dto.request.ProductImageRequest;
import com.example.ecommerce.product.dto.request.ProductRequest;
import com.example.ecommerce.product.dto.request.UpdateStockRequest;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductImage;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.product.mapper.ProductMapper;
import com.example.ecommerce.product.repository.ProductImageRepository;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.product.service.ProductService;
import com.example.ecommerce.product.specification.ProductSpecification;
import com.example.ecommerce.product.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.ecommerce.cache.constant.CacheNames;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Service implementation managing product workflows, pricing calculations,
 * stock status auto-recalculation, gallery media images, brand/category bindings and soft deletion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern DUPLICATE_HYPHENS = Pattern.compile("-+");

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.PRODUCTS, CacheNames.PRODUCT_DETAILS}, allEntries = true)
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product with SKU: {}", request.getSku());
        productValidator.validateForCreate(request);

        Product product = productMapper.toEntity(request);

        // Bind Brand if provided
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findByIdAndDeletedFalse(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + request.getBrandId()));
            product.setBrand(brand);
        }

        // Bind Category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndDeletedFalse(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));
            product.setCategory(category);
        }

        // Auto-generate slug if omitted
        if (!StringUtils.hasText(product.getSlug())) {
            product.setSlug(generateUniqueSlug(request.getName(), null));
        } else {
            product.setSlug(product.getSlug().toLowerCase(Locale.ROOT).trim());
        }

        // Apply inventory & stock status logic
        product.recalculateStockStatus();

        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.ACTIVE);
        }
        product.setActive(product.getStatus() == ProductStatus.ACTIVE);

        // Bind media gallery images
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ProductImage> imageEntities = new ArrayList<>();
            for (ProductImageRequest imgReq : request.getImages()) {
                ProductImage img = productMapper.toImageEntity(imgReq);
                img.setProduct(product);
                imageEntities.add(img);
            }
            product.setImages(imageEntities);
        }

        Product savedProduct = productRepository.save(product);
        log.info("Successfully created product with ID: {}", savedProduct.getId());
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.PRODUCTS, CacheNames.PRODUCT_DETAILS}, allEntries = true)
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product ID: {}", id);
        Product product = findProductEntityById(id);

        productValidator.validateForUpdate(id, request);

        boolean nameChanged = StringUtils.hasText(request.getName()) && !request.getName().trim().equals(product.getName());

        productMapper.updateEntityFromRequest(request, product);

        if (nameChanged && !StringUtils.hasText(request.getSlug())) {
            product.setSlug(generateUniqueSlug(request.getName(), id));
        } else if (StringUtils.hasText(request.getSlug())) {
            product.setSlug(request.getSlug().toLowerCase(Locale.ROOT).trim());
        }

        // Update Brand relationship
        if (request.getBrandId() != null) {
            if (product.getBrand() == null || !request.getBrandId().equals(product.getBrand().getId())) {
                Brand brand = brandRepository.findByIdAndDeletedFalse(request.getBrandId())
                        .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + request.getBrandId()));
                product.setBrand(brand);
            }
        }

        // Update Category relationship
        if (request.getCategoryId() != null) {
            if (product.getCategory() == null || !request.getCategoryId().equals(product.getCategory().getId())) {
                Category category = categoryRepository.findByIdAndDeletedFalse(request.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));
                product.setCategory(category);
            }
        }

        // Update Gallery Images if provided
        if (request.getImages() != null) {
            product.getImages().clear();
            for (ProductImageRequest imgReq : request.getImages()) {
                ProductImage img = productMapper.toImageEntity(imgReq);
                product.addImage(img);
            }
        }

        product.recalculateStockStatus();

        if (request.getStatus() != null) {
            product.setActive(request.getStatus() == ProductStatus.ACTIVE);
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Successfully updated product ID: {}", updatedProduct.getId());
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Cacheable(value = CacheNames.PRODUCT_DETAILS, key = "#id")
    public ProductResponse getProductById(Long id) {
        Product product = findProductEntityById(id);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlugAndDeletedFalse(slug.toLowerCase(Locale.ROOT).trim())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySkuAndDeletedFalse(sku.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return productMapper.toResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getProducts(ProductFilterRequest filter, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.build(filter);
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        return PageResponse.from(productPage, productMapper::toResponse);
    }

    @Override
    @Transactional
    public ProductResponse updateStock(Long id, UpdateStockRequest request) {
        log.info("Updating stock for product ID: {}", id);
        Product product = findProductEntityById(id);

        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }
        if (request.getReservedQuantity() != null) {
            product.setReservedQuantity(request.getReservedQuantity());
        }
        if (request.getLowStock() != null) {
            product.setLowStock(request.getLowStock());
        }

        productValidator.validatePricingAndStock(product.getCostPrice(), product.getSellingPrice(), product.getDiscountPrice(), product.getQuantity(), product.getReservedQuantity());
        product.recalculateStockStatus();

        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public ProductResponse updateStatus(Long id, ProductStatus status) {
        log.info("Updating status for product ID {} to: {}", id, status);
        Product product = findProductEntityById(id);
        product.setStatus(status);
        product.setActive(status == ProductStatus.ACTIVE);
        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    public List<ProductResponse> getFeaturedProducts() {
        List<Product> list = productRepository.findByFeaturedTrueAndStatusAndDeletedFalse(ProductStatus.ACTIVE);
        return productMapper.toResponseList(list);
    }

    @Override
    public List<ProductResponse> getBestsellerProducts() {
        List<Product> list = productRepository.findByBestsellerTrueAndStatusAndDeletedFalse(ProductStatus.ACTIVE);
        return productMapper.toResponseList(list);
    }

    @Override
    public List<ProductResponse> getNewArrivalProducts() {
        List<Product> list = productRepository.findByNewArrivalTrueAndStatusAndDeletedFalse(ProductStatus.ACTIVE);
        return productMapper.toResponseList(list);
    }

    @Override
    public List<ProductResponse> getTrendingProducts() {
        List<Product> list = productRepository.findByTrendingTrueAndStatusAndDeletedFalse(ProductStatus.ACTIVE);
        return productMapper.toResponseList(list);
    }

    @Override
    public List<ProductResponse> getRecommendedProducts() {
        List<Product> list = productRepository.findByRecommendedTrueAndStatusAndDeletedFalse(ProductStatus.ACTIVE);
        return productMapper.toResponseList(list);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Soft deleting product ID: {}", id);
        Product product = findProductEntityById(id);
        product.setDeleted(true);
        product.setDeletedAt(Instant.now());
        product.setStatus(ProductStatus.INACTIVE);
        product.setActive(false);
        product.setStockStatus(StockStatus.OUT_OF_STOCK);
        productRepository.save(product);
        log.info("Successfully soft deleted product ID: {}", id);
    }

    private Product findProductEntityById(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    private String generateUniqueSlug(String text, Long excludeId) {
        String baseSlug = toSlug(text);
        if (!StringUtils.hasText(baseSlug)) {
            baseSlug = "product-" + System.currentTimeMillis();
        }

        String candidateSlug = baseSlug;
        int counter = 1;
        while (isSlugTaken(candidateSlug, excludeId)) {
            candidateSlug = baseSlug + "-" + counter;
            counter++;
        }
        return candidateSlug;
    }

    private boolean isSlugTaken(String slug, Long excludeId) {
        return excludeId == null
                ? productRepository.existsBySlugIgnoreCase(slug)
                : productRepository.existsBySlugIgnoreCaseAndIdNot(slug, excludeId);
    }

    private static String toSlug(String input) {
        if (input == null) {
            return "";
        }
        String nowhitespace = WHITESPACE.matcher(input.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        slug = DUPLICATE_HYPHENS.matcher(slug).replaceAll("-");
        return slug.toLowerCase(Locale.ROOT).replaceAll("^-+|-+$", "");
    }
}
