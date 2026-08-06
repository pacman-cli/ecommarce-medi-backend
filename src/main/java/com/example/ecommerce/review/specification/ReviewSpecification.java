package com.example.ecommerce.review.specification;

import com.example.ecommerce.review.dto.request.ReviewFilterRequest;
import com.example.ecommerce.review.entity.Review;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Specification builder for searching and filtering customer product reviews.
 */
public final class ReviewSpecification {

    private ReviewSpecification() {
    }

    public static Specification<Review> build(ReviewFilterRequest filter) {
        Specification<Review> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);

        if (filter == null) {
            return spec;
        }

        if (filter.getProductId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("product").get("id"), filter.getProductId()));
        }

        if (filter.getRating() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("rating"), filter.getRating()));
        }

        if (filter.getVerifiedOnly() != null && filter.getVerifiedOnly()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("verifiedPurchase"), true));
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getIsReported() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isReported"), filter.getIsReported()));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + filter.getSearch().toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("firstName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("lastName")), pattern)
                );
            });
        }

        return spec;
    }
}
