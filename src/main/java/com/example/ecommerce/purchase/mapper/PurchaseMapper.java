package com.example.ecommerce.purchase.mapper;

import com.example.ecommerce.purchase.dto.response.PurchaseItemResponse;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderListResponse;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderResponse;
import com.example.ecommerce.purchase.entity.PurchaseItem;
import com.example.ecommerce.purchase.entity.PurchaseOrder;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

/**
 * MapStruct mapper for transforming between purchase order entities and response DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface PurchaseMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.sku", target = "productSku")
    PurchaseItemResponse toItemResponse(PurchaseItem item);

    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "supplier.code", target = "supplierCode")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    @Mapping(target = "remainingBalance", expression = "java(computeRemainingBalance(order))")
    @Mapping(target = "totalOrderedQuantity", expression = "java(computeTotalOrderedQuantity(order))")
    @Mapping(target = "totalReceivedQuantity", expression = "java(computeTotalReceivedQuantity(order))")
    PurchaseOrderResponse toResponse(PurchaseOrder order);

    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    @Mapping(target = "totalItemsCount", expression = "java(order.getItems() != null ? order.getItems().size() : 0)")
    PurchaseOrderListResponse toListResponse(PurchaseOrder order);

    default BigDecimal computeRemainingBalance(PurchaseOrder order) {
        if (order == null || order.getTotalAmount() == null) return BigDecimal.ZERO;
        BigDecimal paid = order.getPaidAmount() != null ? order.getPaidAmount() : BigDecimal.ZERO;
        return order.getTotalAmount().subtract(paid).max(BigDecimal.ZERO);
    }

    default Integer computeTotalOrderedQuantity(PurchaseOrder order) {
        if (order == null || order.getItems() == null) return 0;
        return order.getItems().stream()
                .mapToInt(i -> i.getOrderedQuantity() != null ? i.getOrderedQuantity() : 0)
                .sum();
    }

    default Integer computeTotalReceivedQuantity(PurchaseOrder order) {
        if (order == null || order.getItems() == null) return 0;
        return order.getItems().stream()
                .mapToInt(i -> i.getReceivedQuantity() != null ? i.getReceivedQuantity() : 0)
                .sum();
    }
}
