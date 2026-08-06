package com.example.ecommerce.user.specification;

import com.example.ecommerce.user.dto.request.UserFilterRequest;
import com.example.ecommerce.user.entity.Role;
import com.example.ecommerce.user.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Builds composable JPA {@link Specification}s for filtering users by search
 * term, role and account status.
 */
public final class UserSpecification {

    private UserSpecification() {
    }

    /**
     * Returns whether the filter contains at least one criteria.
     *
     * @param filter the filter payload
     * @return {@code true} when filtering is required
     */
    public static boolean hasCriteria(UserFilterRequest filter) {
        if (filter == null) {
            return false;
        }
        return StringUtils.hasText(filter.getSearch())
                || filter.getRole() != null
                || filter.getEnabled() != null;
    }

    /**
     * Builds the filter predicate from the incoming query parameters.
     *
     * @param filter the filter payload (may be {@code null})
     * @return the composed specification
     */
    public static Specification<User> build(UserFilterRequest filter) {
        Specification<User> spec = Specification.where(null);
        if (filter == null) {
            return spec;
        }
        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and(hasSearchTerm(filter.getSearch()));
        }
        if (filter.getRole() != null) {
            spec = spec.and(hasRole(filter.getRole()));
        }
        if (filter.getEnabled() != null) {
            spec = spec.and(hasEnabled(filter.getEnabled()));
        }
        return spec;
    }

    /**
     * Case-insensitive substring match on email, first or last name.
     */
    private static Specification<User> hasSearchTerm(String term) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + term.toLowerCase(Locale.ROOT) + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern));
        };
    }

    private static Specification<User> hasRole(Role role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role);
    }

    private static Specification<User> hasEnabled(Boolean enabled) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), enabled);
    }
}
