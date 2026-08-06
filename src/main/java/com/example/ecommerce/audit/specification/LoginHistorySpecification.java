package com.example.ecommerce.audit.specification;

import com.example.ecommerce.audit.dto.request.LoginHistoryFilterRequest;
import com.example.ecommerce.audit.entity.LoginHistory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * JPA Criteria specification for building dynamic queries against {@link LoginHistory} entities.
 */
public final class LoginHistorySpecification {

    private LoginHistorySpecification() {
    }

    public static Specification<LoginHistory> filterBy(LoginHistoryFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter != null) {
                if (StringUtils.hasText(filter.getUserEmail())) {
                    String pattern = "%" + filter.getUserEmail().toLowerCase(Locale.ROOT).trim() + "%";
                    predicates.add(cb.like(cb.lower(root.get("userEmail")), pattern));
                }

                if (filter.getUserId() != null) {
                    predicates.add(cb.equal(root.get("userId"), filter.getUserId()));
                }

                if (filter.getSuccess() != null) {
                    predicates.add(cb.equal(root.get("success"), filter.getSuccess()));
                }

                if (StringUtils.hasText(filter.getIpAddress())) {
                    predicates.add(cb.equal(root.get("ipAddress"), filter.getIpAddress().trim()));
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
