package com.example.ecommerce.coupon.specification;

import com.example.ecommerce.coupon.dto.request.CouponFilterRequest;
import com.example.ecommerce.coupon.entity.Coupon;
import com.example.ecommerce.coupon.entity.CouponStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Locale;

/**
 * Specification builder for searching and filtering promotional coupons.
 */
public final class CouponSpecification {

    private CouponSpecification() {
    }

    public static Specification<Coupon> build(CouponFilterRequest filter) {
        Specification<Coupon> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);

        if (filter == null) {
            return spec;
        }

        if (filter.getDiscountType() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("discountType"), filter.getDiscountType()));
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getIsAutomatic() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isAutomatic"), filter.getIsAutomatic()));
        }

        if (Boolean.TRUE.equals(filter.getActiveOnly())) {
            Instant now = Instant.now();
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("active"), true),
                            criteriaBuilder.equal(root.get("status"), CouponStatus.ACTIVE),
                            criteriaBuilder.or(
                                    criteriaBuilder.isNull(root.get("startDate")),
                                    criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), now)
                            ),
                            criteriaBuilder.or(
                                    criteriaBuilder.isNull(root.get("endDate")),
                                    criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), now)
                            )
                    ));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + filter.getSearch().toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                );
            });
        }

        return spec;
    }
}
