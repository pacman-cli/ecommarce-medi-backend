package com.example.ecommerce.search.specification;

import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.search.dto.request.SearchFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds composable Spring Data JPA {@link Specification}s for dynamic catalogue searching.
 */
public final class SearchSpecification {

    private SearchSpecification() {
    }

    /**
     * Builds composed Specification predicate based on incoming filter request parameters.
     */
    public static Specification<Product> build(SearchFilterRequest filter) {
        Specification<Product> spec = isNotDeleted().and(isActive());

        if (filter == null) {
            return spec;
        }

        if (StringUtils.hasText(filter.getQuery())) {
            spec = spec.and(hasSearchQuery(filter.getQuery().trim()));
        }

        if (!CollectionUtils.isEmpty(filter.getCategoryIds())) {
            spec = spec.and(hasCategoryIds(filter.getCategoryIds()));
        }

        if (StringUtils.hasText(filter.getCategorySlug())) {
            spec = spec.and(hasCategorySlug(filter.getCategorySlug().trim()));
        }

        if (!CollectionUtils.isEmpty(filter.getBrandIds())) {
            spec = spec.and(hasBrandIds(filter.getBrandIds()));
        }

        if (StringUtils.hasText(filter.getBrandSlug())) {
            spec = spec.and(hasBrandSlug(filter.getBrandSlug().trim()));
        }

        if (filter.getMinPrice() != null || filter.getMaxPrice() != null) {
            spec = spec.and(hasPriceRange(filter.getMinPrice(), filter.getMaxPrice()));
        }

        if (Boolean.TRUE.equals(filter.getInStockOnly())) {
            spec = spec.and(isInStock());
        }

        if (Boolean.TRUE.equals(filter.getHasDiscount())) {
            spec = spec.and(hasDiscount());
        }

        if (filter.getPrescriptionRequired() != null) {
            spec = spec.and(isPrescriptionRequired(filter.getPrescriptionRequired()));
        }

        return spec;
    }

    public static Specification<Product> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("deleted"), false);
    }

    public static Specification<Product> isActive() {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("active"), true),
                cb.equal(root.get("status"), ProductStatus.ACTIVE)
        );
    }

    public static Specification<Product> hasSearchQuery(String searchTerm) {
        return (root, query, cb) -> {
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.like(cb.lower(root.get("name")), pattern));
            predicates.add(cb.like(cb.lower(root.get("genericName")), pattern));
            predicates.add(cb.like(cb.lower(root.get("sku")), pattern));
            predicates.add(cb.like(cb.lower(root.get("shortDescription")), pattern));
            predicates.add(cb.like(cb.lower(root.get("keywords")), pattern));

            // Join category and brand safely
            predicates.add(cb.like(cb.lower(root.join("category", jakarta.persistence.criteria.JoinType.LEFT).get("name")), pattern));
            predicates.add(cb.like(cb.lower(root.join("brand", jakarta.persistence.criteria.JoinType.LEFT).get("name")), pattern));

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> hasCategoryIds(List<Long> categoryIds) {
        return (root, query, cb) -> root.join("category").get("id").in(categoryIds);
    }

    public static Specification<Product> hasCategorySlug(String slug) {
        return (root, query, cb) -> cb.equal(cb.lower(root.join("category").get("slug")), slug.toLowerCase());
    }

    public static Specification<Product> hasBrandIds(List<Long> brandIds) {
        return (root, query, cb) -> root.join("brand").get("id").in(brandIds);
    }

    public static Specification<Product> hasBrandSlug(String slug) {
        return (root, query, cb) -> cb.equal(cb.lower(root.join("brand").get("slug")), slug.toLowerCase());
    }

    public static Specification<Product> hasPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("sellingPrice"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice);
            } else {
                return cb.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice);
            }
        };
    }

    public static Specification<Product> isInStock() {
        return (root, query, cb) -> cb.and(
                cb.greaterThan(root.get("quantity"), 0),
                cb.equal(root.get("stockStatus"), StockStatus.IN_STOCK)
        );
    }

    public static Specification<Product> hasDiscount() {
        return (root, query, cb) -> cb.or(
                cb.isNotNull(root.get("discountPrice")),
                cb.greaterThan(root.get("discountPercentage"), BigDecimal.ZERO)
        );
    }

    public static Specification<Product> isPrescriptionRequired(boolean required) {
        return (root, query, cb) -> cb.equal(root.get("prescriptionRequired"), required);
    }
}
