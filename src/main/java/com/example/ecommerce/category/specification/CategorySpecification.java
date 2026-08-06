package com.example.ecommerce.category.specification;

import com.example.ecommerce.category.dto.request.CategoryFilterRequest;
import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.category.entity.CategoryStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Builds composable JPA {@link Specification}s for dynamic filtering of categories.
 */
public final class CategorySpecification {

    private CategorySpecification() {
    }

    /**
     * Builds the composed specification from filter request options.
     *
     * @param filter the filter parameters (may be {@code null})
     * @return the combined JPA specification predicate
     */
    public static Specification<Category> build(CategoryFilterRequest filter) {
        Specification<Category> spec = isNotDeleted();

        if (filter == null) {
            return spec;
        }

        // By default, restrict to ACTIVE categories unless explicit flag allows inactive
        if (filter.getIncludeInactive() == null || !filter.getIncludeInactive()) {
            if (filter.getStatus() == null) {
                spec = spec.and(hasStatus(CategoryStatus.ACTIVE));
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

        if (filter.getParentId() != null) {
            spec = spec.and(hasParentId(filter.getParentId()));
        } else if (Boolean.TRUE.equals(filter.getRootOnly())) {
            spec = spec.and(isRoot());
        }

        if (filter.getFeatured() != null) {
            spec = spec.and(isFeatured(filter.getFeatured()));
        }

        return spec;
    }

    private static Specification<Category> isNotDeleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);
    }

    private static Specification<Category> hasStatus(CategoryStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    private static Specification<Category> hasSearchTerm(String search) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + search.toLowerCase(Locale.ROOT) + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
            );
        };
    }

    private static Specification<Category> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase(Locale.ROOT) + "%");
    }

    private static Specification<Category> hasSlug(String slug) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("slug")), slug.toLowerCase(Locale.ROOT));
    }

    private static Specification<Category> hasParentId(Long parentId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("parent").get("id"), parentId);
    }

    private static Specification<Category> isRoot() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("parent"));
    }

    private static Specification<Category> isFeatured(Boolean featured) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("featured"), featured);
    }
}
