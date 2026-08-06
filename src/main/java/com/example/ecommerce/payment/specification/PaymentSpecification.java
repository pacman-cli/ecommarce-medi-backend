package com.example.ecommerce.payment.specification;

import com.example.ecommerce.payment.dto.request.PaymentFilterRequest;
import com.example.ecommerce.payment.entity.Payment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Specification builder for querying payment records.
 */
public final class PaymentSpecification {

    private PaymentSpecification() {
    }

    public static Specification<Payment> build(PaymentFilterRequest filter) {
        Specification<Payment> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);

        if (filter == null) {
            return spec;
        }

        if (filter.getPaymentMethod() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("paymentMethod"), filter.getPaymentMethod()));
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

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + filter.getSearch().toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("transactionId")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("gatewayTransactionId")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("order").get("orderNumber")), pattern)
                );
            });
        }

        return spec;
    }
}
