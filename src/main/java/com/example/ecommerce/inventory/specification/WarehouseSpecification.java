package com.example.ecommerce.inventory.specification;

import com.example.ecommerce.inventory.entity.Warehouse;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Specification builder for {@link Warehouse} search and filtering.
 */
public final class WarehouseSpecification {

    private WarehouseSpecification() {
    }

    public static Specification<Warehouse> build(String search, Boolean activeOnly) {
        Specification<Warehouse> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false);

        if (activeOnly != null && activeOnly) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), true));
        }

        if (StringUtils.hasText(search)) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + search.toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("contactPerson")), pattern)
                );
            });
        }

        return spec;
    }
}
