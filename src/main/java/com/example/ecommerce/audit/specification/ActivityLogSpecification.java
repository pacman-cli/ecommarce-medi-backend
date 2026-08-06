package com.example.ecommerce.audit.specification;

import com.example.ecommerce.audit.dto.request.ActivityLogFilterRequest;
import com.example.ecommerce.audit.entity.ActivityLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * JPA Criteria specification for building dynamic queries against {@link ActivityLog} entities.
 */
public final class ActivityLogSpecification {

    private ActivityLogSpecification() {
    }

    public static Specification<ActivityLog> filterBy(ActivityLogFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter != null) {
                if (filter.getActivityType() != null) {
                    predicates.add(cb.equal(root.get("activityType"), filter.getActivityType()));
                }

                if (StringUtils.hasText(filter.getModule())) {
                    predicates.add(cb.equal(cb.lower(root.get("module")), filter.getModule().toLowerCase(Locale.ROOT).trim()));
                }

                if (filter.getUserId() != null) {
                    predicates.add(cb.equal(root.get("userId"), filter.getUserId()));
                }

                if (filter.getIsAdminActivity() != null) {
                    predicates.add(cb.equal(root.get("isAdminActivity"), filter.getIsAdminActivity()));
                }

                if (StringUtils.hasText(filter.getQuery())) {
                    String pattern = "%" + filter.getQuery().toLowerCase(Locale.ROOT).trim() + "%";
                    Predicate descLike = cb.like(cb.lower(root.get("description")), pattern);
                    Predicate metaLike = cb.like(cb.lower(root.get("metadata")), pattern);
                    Predicate userLike = cb.like(cb.lower(root.get("username")), pattern);
                    predicates.add(cb.or(descLike, metaLike, userLike));
                }

                if (filter.getStartDate() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), filter.getStartDate()));
                }

                if (filter.getEndDate() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), filter.getEndDate()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
