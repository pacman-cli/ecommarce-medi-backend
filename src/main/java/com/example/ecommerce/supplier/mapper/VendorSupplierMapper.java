package com.example.ecommerce.supplier.mapper;

import com.example.ecommerce.inventory.entity.Supplier;
import com.example.ecommerce.supplier.dto.request.SupplierRequest;
import com.example.ecommerce.supplier.dto.response.SupplierResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for converting between {@link Supplier} entities and response DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface VendorSupplierMapper {

    @Mapping(source = "name", target = "companyName")
    SupplierResponse toResponse(Supplier supplier);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Supplier toEntity(SupplierRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromRequest(SupplierRequest request, @MappingTarget Supplier supplier);

    default List<SupplierResponse> toResponseList(List<Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return Collections.emptyList();
        }
        return suppliers.stream()
                .filter(s -> !s.isDeleted())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
