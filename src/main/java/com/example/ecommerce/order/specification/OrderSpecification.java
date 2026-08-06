package com.example.ecommerce.order.specification;

import com.example.ecommerce.order.dto.request.OrderFilterRequest;
import com.example.ecommerce.order.entity.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Specification builder for searching and filtering orders.
 */
public final class OrderSpecification {

    private OrderSpecification() {
    }

    public static Specification<Order> build(OrderFilterRequest filter, Long userId) {
        Specification<Order> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);

        if (userId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("user").get("id"), userId));
        }

        if (filter == null) {
            return spec;
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getPaymentStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("paymentStatus"), filter.getPaymentStatus()));
        }

        if (filter.getStartDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filter.getStartDate()));
        }

        if (filter.getEndDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filter.getEndDate()));
        }

        if (filter.getMinAmount() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("grandTotal"), filter.getMinAmount()));
        }

        if (filter.getMaxAmount() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("grandTotal"), filter.getMaxAmount()));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + filter.getSearch().toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("orderNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("invoiceNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("shippingAddress").get("recipientName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("shippingAddress").get("phone")), pattern)
                );
            });
        }

        return spec;
    }
}
