package com.example.ecommerce.brand.specification;

import com.example.ecommerce.brand.dto.request.BrandFilterRequest;
import com.example.ecommerce.brand.entity.Brand;
import com.example.ecommerce.brand.entity.BrandStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Builds composable JPA {@link Specification}s for dynamic filtering and searching of brands.
 */
public final class BrandSpecification {

    private BrandSpecification() {
    }

    /**
     * Builds the composed specification from filter request options.
     *
     * @param filter the filter parameters (may be {@code null})
     * @return the combined JPA specification predicate
     */
    public static Specification<Brand> build(BrandFilterRequest filter) {
        Specification<Brand> spec = isNotDeleted();

        if (filter == null) {
            return spec;
        }

        // Default to ACTIVE brands unless explicit flag allows inactive
        if (filter.getIncludeInactive() == null || !filter.getIncludeInactive()) {
            if (filter.getStatus() == null) {
                spec = spec.and(hasStatus(BrandStatus.ACTIVE));
            }
        }

        if (filter.getStatus() != null) {
            spec = spec.and(hasStatus(filter.getStatus()));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and(hasSearchTerm(filter.getSearch()));
        }

        if (StringUtils.hasText(filter.getName())) {
            spec = spec.and(hasName(filter.getName()));
        }

        if (StringUtils.hasText(filter.getSlug())) {
            spec = spec.and(hasSlug(filter.getSlug()));
        }

        if (StringUtils.hasText(filter.getCountry())) {
            spec = spec.and(hasCountry(filter.getCountry()));
        }

        if (filter.getFeatured() != null) {
            spec = spec.and(isFeatured(filter.getFeatured()));
        }

        return spec;
    }

    private static Specification<Brand> isNotDeleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);
    }

    private static Specification<Brand> hasStatus(BrandStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    private static Specification<Brand> hasSearchTerm(String search) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + search.toLowerCase(Locale.ROOT) + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), pattern)
            );
        };
    }

    private static Specification<Brand> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase(Locale.ROOT) + "%");
    }

    private static Specification<Brand> hasSlug(String slug) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("slug")), slug.toLowerCase(Locale.ROOT));
    }

    private static Specification<Brand> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), "%" + country.toLowerCase(Locale.ROOT) + "%");
    }

    private static Specification<Brand> isFeatured(Boolean featured) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("featured"), featured);
    }
}
