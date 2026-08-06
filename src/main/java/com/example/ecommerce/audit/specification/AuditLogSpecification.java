package com.example.ecommerce.audit.specification;

import com.example.ecommerce.audit.dto.request.AuditLogFilterRequest;
import com.example.ecommerce.audit.entity.AuditLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * JPA Criteria specification for building dynamic queries against {@link AuditLog} entities.
 */
public final class AuditLogSpecification {

    private AuditLogSpecification() {
    }

    public static Specification<AuditLog> filterBy(AuditLogFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter != null) {
                if (StringUtils.hasText(filter.getEntityName())) {
                    predicates.add(cb.equal(cb.lower(root.get("entityName")), filter.getEntityName().toLowerCase(Locale.ROOT).trim()));
                }

                if (StringUtils.hasText(filter.getEntityId())) {
                    predicates.add(cb.equal(root.get("entityId"), filter.getEntityId().trim()));
                }

                if (filter.getAction() != null) {
                    predicates.add(cb.equal(root.get("action"), filter.getAction()));
                }

                if (filter.getUserId() != null) {
                    predicates.add(cb.equal(root.get("userId"), filter.getUserId()));
                }

                if (StringUtils.hasText(filter.getUsername())) {
                    String pattern = "%" + filter.getUsername().toLowerCase(Locale.ROOT).trim() + "%";
                    predicates.add(cb.like(cb.lower(root.get("username")), pattern));
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
