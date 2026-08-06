package com.example.ecommerce.inventory.mapper;

import com.example.ecommerce.inventory.dto.request.StockBatchRequest;
import com.example.ecommerce.inventory.dto.response.InventoryTransactionResponse;
import com.example.ecommerce.inventory.dto.response.StockBatchResponse;
import com.example.ecommerce.inventory.entity.InventoryTransaction;
import com.example.ecommerce.inventory.entity.StockBatch;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for {@link StockBatch} and {@link InventoryTransaction} conversions.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface InventoryMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    StockBatchResponse toBatchResponse(StockBatch stockBatch);

    List<StockBatchResponse> toBatchResponseList(List<StockBatch> stockBatches);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "availableQuantity", ignore = true)
    @Mapping(target = "reservedQuantity", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    StockBatch toBatchEntity(StockBatchRequest request);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(source = "stockBatch.id", target = "stockBatchId")
    @Mapping(source = "stockBatch.batchNumber", target = "batchNumber")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    InventoryTransactionResponse toTransactionResponse(InventoryTransaction transaction);

    List<InventoryTransactionResponse> toTransactionResponseList(List<InventoryTransaction> transactions);
}
