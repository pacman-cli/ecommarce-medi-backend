package com.example.ecommerce.product.specification;

import com.example.ecommerce.product.dto.request.ProductFilterRequest;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Builds composable JPA {@link Specification}s for full-text search, multi-criteria filtering,
 * price ranges, brands, categories and promotional flags.
 */
public final class ProductSpecification {

    private ProductSpecification() {
    }

    /**
     * Builds the composed specification based on incoming filter criteria.
     *
     * @param filter the filter parameters (may be {@code null})
     * @return the composed specification predicate
     */
    public static Specification<Product> build(ProductFilterRequest filter) {
        Specification<Product> spec = isNotDeleted();

        if (filter == null) {
            return spec;
        }

        // Restrict to ACTIVE products by default unless explicit flag allows inactive
        if (filter.getIncludeInactive() == null || !filter.getIncludeInactive()) {
            if (filter.getStatus() == null) {
                spec = spec.and(hasStatus(ProductStatus.ACTIVE));
            }
        }

        if (filter.getStatus() != null) {
            spec = spec.and(hasStatus(filter.getStatus()));
        }

        if (filter.getStockStatus() != null) {
            spec = spec.and(hasStockStatus(filter.getStockStatus()));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and(hasSearchTerm(filter.getSearch()));
        }

        if (StringUtils.hasText(filter.getName())) {
            spec = spec.and(hasName(filter.getName()));
        }

        if (StringUtils.hasText(filter.getSku())) {
            spec = spec.and(hasSku(filter.getSku()));
        }

        if (StringUtils.hasText(filter.getBarcode())) {
            spec = spec.and(hasBarcode(filter.getBarcode()));
        }

        if (filter.getBrandId() != null) {
            spec = spec.and(hasBrandId(filter.getBrandId()));
        }

        if (filter.getCategoryId() != null) {
            spec = spec.and(hasCategoryId(filter.getCategoryId()));
        }

        if (filter.getMinPrice() != null) {
            spec = spec.and(priceGreaterThanOrEqualTo(filter.getMinPrice()));
        }

        if (filter.getMaxPrice() != null) {
            spec = spec.and(priceLessThanOrEqualTo(filter.getMaxPrice()));
        }

        if (filter.getPrescriptionRequired() != null) {
            spec = spec.and(isPrescriptionRequired(filter.getPrescriptionRequired()));
        }

        if (filter.getFeatured() != null) {
            spec = spec.and(isFeatured(filter.getFeatured()));
        }

        if (filter.getBestseller() != null) {
            spec = spec.and(isBestseller(filter.getBestseller()));
        }

        if (filter.getNewArrival() != null) {
            spec = spec.and(isNewArrival(filter.getNewArrival()));
        }

        if (filter.getTrending() != null) {
            spec = spec.and(isTrending(filter.getTrending()));
        }

        if (filter.getRecommended() != null) {
            spec = spec.and(isRecommended(filter.getRecommended()));
        }

        return spec;
    }

    private static Specification<Product> isNotDeleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);
    }

    private static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    private static Specification<Product> hasStockStatus(StockStatus stockStatus) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("stockStatus"), stockStatus);
    }

    private static Specification<Product> hasSearchTerm(String search) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + search.toLowerCase(Locale.ROOT) + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("barcode")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("genericName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("manufacturer")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("keywords")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("brand").get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("name")), pattern)
            );
        };
    }

    private static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase(Locale.ROOT) + "%");
    }

    private static Specification<Product> hasSku(String sku) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("sku")), sku.toLowerCase(Locale.ROOT));
    }

    private static Specification<Product> hasBarcode(String barcode) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("barcode")), barcode.toLowerCase(Locale.ROOT));
    }

    private static Specification<Product> hasBrandId(Long brandId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("brand").get("id"), brandId);
    }

    private static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    private static Specification<Product> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice);
    }

    private static Specification<Product> priceLessThanOrEqualTo(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice);
    }

    private static Specification<Product> isPrescriptionRequired(Boolean prescriptionRequired) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("prescriptionRequired"), prescriptionRequired);
    }

    private static Specification<Product> isFeatured(Boolean featured) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("featured"), featured);
    }

    private static Specification<Product> isBestseller(Boolean bestseller) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("bestseller"), bestseller);
    }

    private static Specification<Product> isNewArrival(Boolean newArrival) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("newArrival"), newArrival);
    }

    private static Specification<Product> isTrending(Boolean trending) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("trending"), trending);
    }

    private static Specification<Product> isRecommended(Boolean recommended) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("recommended"), recommended);
    }
}
