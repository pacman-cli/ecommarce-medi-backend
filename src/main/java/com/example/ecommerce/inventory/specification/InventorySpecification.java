package com.example.ecommerce.inventory.specification;

import com.example.ecommerce.inventory.dto.request.InventoryFilterRequest;
import com.example.ecommerce.inventory.entity.BatchStatus;
import com.example.ecommerce.inventory.entity.InventoryTransaction;
import com.example.ecommerce.inventory.entity.StockBatch;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Specification builders for {@link StockBatch} and {@link InventoryTransaction}.
 */
public final class InventorySpecification {

    private InventorySpecification() {
    }

    public static Specification<StockBatch> buildBatchSpec(InventoryFilterRequest filter) {
        Specification<StockBatch> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false);

        if (filter == null) {
            return spec;
        }

        if (filter.getProductId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("id"), filter.getProductId()));
        }

        if (filter.getWarehouseId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("warehouse").get("id"), filter.getWarehouseId()));
        }

        if (filter.getSupplierId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("supplier").get("id"), filter.getSupplierId()));
        }

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }

        if (StringUtils.hasText(filter.getBarcode())) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("barcode")), filter.getBarcode().toLowerCase(Locale.ROOT)));
        }

        if (StringUtils.hasText(filter.getQrCode())) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("qrCode")), filter.getQrCode().toLowerCase(Locale.ROOT)));
        }

        if (Boolean.TRUE.equals(filter.getLowStockOnly())) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), BatchStatus.LOW_STOCK));
        }

        if (Boolean.TRUE.equals(filter.getExpiredOnly())) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("status"), BatchStatus.EXPIRED),
                    criteriaBuilder.lessThan(root.get("expiryDate"), LocalDate.now())
            ));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + filter.getSearch().toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("batchNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("barcode")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("sku")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("warehouse").get("name")), pattern)
                );
            });
        }

        return spec;
    }

    public static Specification<InventoryTransaction> buildTransactionSpec(InventoryFilterRequest filter) {
        Specification<InventoryTransaction> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (filter == null) {
            return spec;
        }

        if (filter.getProductId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("id"), filter.getProductId()));
        }

        if (filter.getWarehouseId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("warehouse").get("id"), filter.getWarehouseId()));
        }

        if (filter.getSupplierId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("supplier").get("id"), filter.getSupplierId()));
        }

        if (filter.getTransactionType() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("transactionType"), filter.getTransactionType()));
        }

        if (StringUtils.hasText(filter.getSearch())) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                String pattern = "%" + filter.getSearch().toLowerCase(Locale.ROOT) + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("referenceNumber")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("reason")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("performedBy")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("sku")), pattern)
                );
            });
        }

        return spec;
    }
}
