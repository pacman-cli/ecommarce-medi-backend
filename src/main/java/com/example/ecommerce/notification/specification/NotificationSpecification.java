package com.example.ecommerce.notification.specification;

import com.example.ecommerce.notification.dto.request.NotificationFilterRequest;
import com.example.ecommerce.notification.entity.Notification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Specification builder for searching and filtering notification audit records.
 */
public final class NotificationSpecification {

    private NotificationSpecification() {
    }

    public static Specification<Notification> build(NotificationFilterRequest filter) {
        Specification<Notification> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);

        if (filter == null) {
            return spec;
        }

        if (filter.getUserId() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("user").get("id"), filter.getUserId()));
        }

        if (filter.getChannel() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("channel"), filter.getChannel()));
        }

        if (filter.getType() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), filter.getType()));
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + filter.getSearch().toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("recipient")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), pattern)
                );
            });
        }

        return spec;
    }
}
