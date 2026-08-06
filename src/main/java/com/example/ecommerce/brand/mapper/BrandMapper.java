package com.example.ecommerce.brand.mapper;

import com.example.ecommerce.brand.dto.request.BrandRequest;
import com.example.ecommerce.brand.dto.response.BrandResponse;
import com.example.ecommerce.brand.entity.Brand;
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
 * MapStruct mapper for converting between {@link Brand} entity and DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface BrandMapper {

    /**
     * Translates a {@link Brand} entity into a detailed {@link BrandResponse}.
     *
     * @param brand the persistent entity
     * @return the detailed response DTO
     */
    BrandResponse toResponse(Brand brand);

    /**
     * Maps a list of brand entities to response DTOs.
     *
     * @param brands list of brand entities
     * @return list of brand response DTOs
     */
    default List<BrandResponse> toResponseList(List<Brand> brands) {
        if (brands == null || brands.isEmpty()) {
            return Collections.emptyList();
        }
        return brands.stream()
                .filter(b -> !b.isDeleted())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a creation request DTO to a transient {@link Brand} entity.
     *
     * @param request the brand request payload
     * @return a new transient brand entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    Brand toEntity(BrandRequest request);

    /**
     * Merges non-null properties from request DTO into an existing brand entity.
     *
     * @param request the request payload
     * @param brand   the target brand entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(BrandRequest request, @MappingTarget Brand brand);
}
