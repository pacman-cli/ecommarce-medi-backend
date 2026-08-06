package com.example.ecommerce.inventory.specification;

import com.example.ecommerce.inventory.entity.Supplier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Specification builder for {@link Supplier} search and filtering.
 */
public final class SupplierSpecification {

    private SupplierSpecification() {
    }

    public static Specification<Supplier> build(String search, Boolean activeOnly) {
        Specification<Supplier> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false);

        if (activeOnly != null && activeOnly) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), true));
        }

        if (StringUtils.hasText(search)) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + search.toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("contactPerson")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("taxNumber")), pattern)
                );
            });
        }

        return spec;
    }
}
