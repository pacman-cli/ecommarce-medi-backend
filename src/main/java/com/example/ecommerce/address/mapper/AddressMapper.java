package com.example.ecommerce.address.mapper;

import com.example.ecommerce.address.dto.request.AddressRequest;
import com.example.ecommerce.address.dto.response.AddressResponse;
import com.example.ecommerce.user.entity.Address;
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
 * MapStruct mapper for transforming between {@link Address} entities and DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface AddressMapper {

    @Mapping(source = "user.id", target = "userId")
    AddressResponse toResponse(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Address toEntity(AddressRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromRequest(AddressRequest request, @MappingTarget Address address);

    default List<AddressResponse> toResponseList(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return Collections.emptyList();
        }
        return addresses.stream()
                .filter(a -> !a.isDeleted())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
