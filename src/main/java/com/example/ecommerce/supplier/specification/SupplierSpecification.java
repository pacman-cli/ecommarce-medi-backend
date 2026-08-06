package com.example.ecommerce.supplier.specification;

import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.supplier.dto.request.SupplierFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * JPA Criteria specification for building dynamic queries against {@link Supplier} entities.
 */
public final class SupplierSpecification {

    private SupplierSpecification() {
    }

    public static Specification<Supplier> filterBy(SupplierFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("deleted"), false));

            if (filter != null) {
                if (StringUtils.hasText(filter.getQuery())) {
                    String pattern = "%" + filter.getQuery().toLowerCase(Locale.ROOT).trim() + "%";
                    Predicate nameLike = cb.like(cb.lower(root.get("name")), pattern);
                    Predicate codeLike = cb.like(cb.lower(root.get("code")), pattern);
                    Predicate contactLike = cb.like(cb.lower(root.get("contactPerson")), pattern);
                    Predicate emailLike = cb.like(cb.lower(root.get("email")), pattern);
                    predicates.add(cb.or(nameLike, codeLike, contactLike, emailLike));
                }

                if (filter.getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), filter.getStatus()));
                }

                if (StringUtils.hasText(filter.getTradeLicense())) {
                    String pattern = "%" + filter.getTradeLicense().toLowerCase(Locale.ROOT).trim() + "%";
                    predicates.add(cb.like(cb.lower(root.get("tradeLicense")), pattern));
                }

                if (StringUtils.hasText(filter.getTin())) {
                    String pattern = "%" + filter.getTin().toLowerCase(Locale.ROOT).trim() + "%";
                    predicates.add(cb.like(cb.lower(root.get("tin")), pattern));
                }

                if (filter.getActive() != null) {
                    predicates.add(cb.equal(root.get("active"), filter.getActive()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
