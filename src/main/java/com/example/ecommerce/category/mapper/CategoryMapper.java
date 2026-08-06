package com.example.ecommerce.category.mapper;

import com.example.ecommerce.category.dto.request.CategoryRequest;
import com.example.ecommerce.category.dto.response.CategoryResponse;
import com.example.ecommerce.category.dto.response.CategoryTreeResponse;
import com.example.ecommerce.category.entity.Category;
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
 * MapStruct mapper for converting between {@link Category} entity and DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface CategoryMapper {

    /**
     * Translates a {@link Category} entity into a detailed {@link CategoryResponse}.
     *
     * @param category the persistent entity
     * @return the detailed response DTO
     */
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    @Mapping(source = "parent.slug", target = "parentSlug")
    @Mapping(target = "childrenCount", expression = "java(category.getChildren() != null ? category.getChildren().size() : 0)")
    @Mapping(target = "children", expression = "java(toResponseList(category.getChildren()))")
    CategoryResponse toResponse(Category category);

    /**
     * Converts a category entity into a lightweight tree node response.
     *
     * @param category the persistent entity
     * @return the tree response DTO
     */
    @Mapping(target = "children", expression = "java(toTreeResponseList(category.getChildren()))")
    CategoryTreeResponse toTreeResponse(Category category);

    /**
     * Maps a list of entities to detailed responses.
     *
     * @param categories list of category entities
     * @return list of category response DTOs
     */
    default List<CategoryResponse> toResponseList(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream()
                .filter(c -> !c.isDeleted())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of entities to tree responses.
     *
     * @param categories list of category entities
     * @return list of category tree response DTOs
     */
    default List<CategoryTreeResponse> toTreeResponseList(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream()
                .filter(c -> !c.isDeleted())
                .map(this::toTreeResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a creation request DTO to a transient {@link Category} entity.
     *
     * @param request the category request payload
     * @return a new transient category entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    Category toEntity(CategoryRequest request);

    /**
     * Merges non-null properties from request DTO into an existing category entity.
     *
     * @param request  the request payload
     * @param category the target category entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category category);
}
