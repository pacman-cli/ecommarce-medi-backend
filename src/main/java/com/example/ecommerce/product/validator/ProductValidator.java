package com.example.ecommerce.product.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.product.dto.request.ProductRequest;
import com.example.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Enterprise validator enforcing product business constraints, uniqueness rules,
 * pricing sanity checks and stock invariants.
 */
@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;

    /**
     * Validates constraints when creating a new product.
     *
     * @param request creation request payload
     */
    public void validateForCreate(ProductRequest request) {
        validateNameUniqueness(request.getName(), null);
        validateSkuUniqueness(request.getSku(), null);
        if (StringUtils.hasText(request.getSlug())) {
            validateSlugUniqueness(request.getSlug(), null);
        }
        if (StringUtils.hasText(request.getBarcode())) {
            validateBarcodeUniqueness(request.getBarcode(), null);
        }
        validatePricingAndStock(request.getCostPrice(), request.getSellingPrice(), request.getDiscountPrice(), request.getQuantity(), request.getReservedQuantity());
    }

    /**
     * Validates constraints when updating an existing product.
     *
     * @param productId ID of the product being updated
     * @param request   update request payload
     */
    public void validateForUpdate(Long productId, ProductRequest request) {
        if (StringUtils.hasText(request.getName())) {
            validateNameUniqueness(request.getName(), productId);
        }
        if (StringUtils.hasText(request.getSku())) {
            validateSkuUniqueness(request.getSku(), productId);
        }
        if (StringUtils.hasText(request.getSlug())) {
            validateSlugUniqueness(request.getSlug(), productId);
        }
        if (StringUtils.hasText(request.getBarcode())) {
            validateBarcodeUniqueness(request.getBarcode(), productId);
        }
        validatePricingAndStock(request.getCostPrice(), request.getSellingPrice(), request.getDiscountPrice(), request.getQuantity(), request.getReservedQuantity());
    }

    /**
     * Validates pricing and stock invariants.
     */
    public void validatePricingAndStock(BigDecimal costPrice, BigDecimal sellingPrice, BigDecimal discountPrice, Integer quantity, Integer reservedQuantity) {
        if (sellingPrice != null && sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Selling price must be non-negative");
        }
        if (costPrice != null && costPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Cost price must be non-negative");
        }
        if (discountPrice != null) {
            if (discountPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("Discount price must be non-negative");
            }
            if (sellingPrice != null && discountPrice.compareTo(sellingPrice) > 0) {
                throw new BadRequestException("Discount price cannot be higher than selling price");
            }
        }
        if (quantity != null && quantity < 0) {
            throw new BadRequestException("Stock quantity must be non-negative");
        }
        if (reservedQuantity != null && reservedQuantity < 0) {
            throw new BadRequestException("Reserved stock quantity must be non-negative");
        }
        if (quantity != null && reservedQuantity != null && reservedQuantity > quantity) {
            throw new BadRequestException("Reserved quantity cannot exceed total stock quantity");
        }
    }

    public void validateNameUniqueness(String name, Long excludeId) {
        String trimmed = name.trim();
        boolean exists = excludeId == null
                ? productRepository.existsByNameIgnoreCase(trimmed)
                : productRepository.existsByNameIgnoreCaseAndIdNot(trimmed, excludeId);
        if (exists) {
            throw new ConflictException("Product with name '" + trimmed + "' already exists");
        }
    }

    public void validateSkuUniqueness(String sku, Long excludeId) {
        String trimmed = sku.trim();
        boolean exists = excludeId == null
                ? productRepository.existsBySkuIgnoreCase(trimmed)
                : productRepository.existsBySkuIgnoreCaseAndIdNot(trimmed, excludeId);
        if (exists) {
            throw new ConflictException("Product SKU '" + trimmed + "' already exists");
        }
    }

    public void validateSlugUniqueness(String slug, Long excludeId) {
        String trimmed = slug.trim().toLowerCase();
        boolean exists = excludeId == null
                ? productRepository.existsBySlugIgnoreCase(trimmed)
                : productRepository.existsBySlugIgnoreCaseAndIdNot(trimmed, excludeId);
        if (exists) {
            throw new ConflictException("Product slug '" + trimmed + "' already exists");
        }
    }

    public void validateBarcodeUniqueness(String barcode, Long excludeId) {
        String trimmed = barcode.trim();
        boolean exists = excludeId == null
                ? productRepository.existsByBarcodeIgnoreCase(trimmed)
                : productRepository.existsByBarcodeIgnoreCaseAndIdNot(trimmed, excludeId);
        if (exists) {
            throw new ConflictException("Product barcode '" + trimmed + "' already exists");
        }
    }
}
