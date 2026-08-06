package com.example.ecommerce.product.mapper;

import com.example.ecommerce.product.dto.request.ProductImageRequest;
import com.example.ecommerce.product.dto.request.ProductRequest;
import com.example.ecommerce.product.dto.response.ProductImageResponse;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductImage;
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
 * MapStruct mapper for converting between {@link Product} / {@link ProductImage} entities and DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface ProductMapper {

    /**
     * Maps a product entity to a response DTO.
     */
    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "brand.name", target = "brandName")
    @Mapping(source = "brand.slug", target = "brandSlug")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.slug", target = "categorySlug")
    @Mapping(source = "images", target = "images")
    ProductResponse toResponse(Product product);

    /**
     * Maps a product image entity to a response DTO.
     */
    ProductImageResponse toImageResponse(ProductImage image);

    /**
     * Maps a list of product entities to response DTOs.
     */
    default List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }
        return products.stream()
                .filter(p -> !p.isDeleted())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a creation request DTO to a transient {@link Product} entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    Product toEntity(ProductRequest request);

    /**
     * Maps a product image request DTO to a transient {@link ProductImage} entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductImage toImageEntity(ProductImageRequest request);

    /**
     * Merges non-null properties from request DTO into an existing product entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product product);
}
