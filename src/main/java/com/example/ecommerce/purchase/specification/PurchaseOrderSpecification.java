package com.example.ecommerce.purchase.specification;

import com.example.ecommerce.purchase.dto.request.PurchaseOrderFilterRequest;
import com.example.ecommerce.purchase.entity.PurchaseOrder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * JPA Criteria specification for building dynamic queries against {@link PurchaseOrder} entities.
 */
public final class PurchaseOrderSpecification {

    private PurchaseOrderSpecification() {
    }

    public static Specification<PurchaseOrder> filterBy(PurchaseOrderFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("deleted"), false));

            if (filter != null) {
                if (StringUtils.hasText(filter.getQuery())) {
                    String pattern = "%" + filter.getQuery().toLowerCase(Locale.ROOT).trim() + "%";
                    Predicate poLike = cb.like(cb.lower(root.get("poNumber")), pattern);
                    Predicate invLike = cb.like(cb.lower(root.get("invoiceNumber")), pattern);
                    Predicate supplierNameLike = cb.like(cb.lower(root.get("supplier").get("name")), pattern);
                    Predicate supplierCodeLike = cb.like(cb.lower(root.get("supplier").get("code")), pattern);
                    predicates.add(cb.or(poLike, invLike, supplierNameLike, supplierCodeLike));
                }

                if (filter.getSupplierId() != null) {
                    predicates.add(cb.equal(root.get("supplier").get("id"), filter.getSupplierId()));
                }

                if (filter.getWarehouseId() != null) {
                    predicates.add(cb.equal(root.get("warehouse").get("id"), filter.getWarehouseId()));
                }

                if (filter.getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), filter.getStatus()));
                }

                if (filter.getPaymentStatus() != null) {
                    predicates.add(cb.equal(root.get("paymentStatus"), filter.getPaymentStatus()));
                }

                if (filter.getStartDate() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), filter.getStartDate()));
                }

                if (filter.getEndDate() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("orderDate"), filter.getEndDate()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
