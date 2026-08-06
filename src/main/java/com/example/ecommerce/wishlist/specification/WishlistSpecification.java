package com.example.ecommerce.wishlist.specification;

import com.example.ecommerce.wishlist.dto.request.WishlistFilterRequest;
import com.example.ecommerce.wishlist.entity.WishlistItem;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Specification builder for querying wishlist items.
 */
public final class WishlistSpecification {

    private WishlistSpecification() {
    }

    public static Specification<WishlistItem> build(Long userId, WishlistFilterRequest filter) {
        Specification<WishlistItem> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("wishlist").get("user").get("id"), userId);

        if (filter == null) {
            return spec;
        }

        if (filter.getCategoryId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("product").get("category").get("id"), filter.getCategoryId()));
        }

        if (filter.getBrandId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("product").get("brand").get("id"), filter.getBrandId()));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + filter.getSearch().toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("sku")), pattern)
                );
            });
        }

        return spec;
    }
}
