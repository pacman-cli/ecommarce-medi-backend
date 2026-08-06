package com.example.ecommerce.category.validator;

import com.example.ecommerce.category.dto.request.CategoryRequest;
import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.category.repository.CategoryRepository;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Enterprise validator enforcing category business rules, uniqueness,
 * parent validity and circular reference prevention for unlimited nesting.
 */
@Component
@RequiredArgsConstructor
public class CategoryValidator {

    private final CategoryRepository categoryRepository;

    /**
     * Validates business constraints when creating a new category.
     *
     * @param request the category creation request payload
     */
    public void validateForCreate(CategoryRequest request) {
        validateNameUniqueness(request.getName(), null);
        if (StringUtils.hasText(request.getSlug())) {
            validateSlugUniqueness(request.getSlug(), null);
        }
        if (request.getParentId() != null) {
            validateParentCategory(request.getParentId(), null);
        }
    }

    /**
     * Validates business constraints when updating an existing category.
     *
     * @param categoryId the ID of the category being updated
     * @param request    the category update request payload
     */
    public void validateForUpdate(Long categoryId, CategoryRequest request) {
        if (request.getName() != null) {
            validateNameUniqueness(request.getName(), categoryId);
        }
        if (StringUtils.hasText(request.getSlug())) {
            validateSlugUniqueness(request.getSlug(), categoryId);
        }
        if (request.getParentId() != null) {
            validateParentCategory(request.getParentId(), categoryId);
        }
    }

    /**
     * Ensures parent exists, is not soft-deleted, and does not cause a circular reference hierarchy.
     *
     * @param parentId          the target parent category ID
     * @param currentCategoryId the ID of the category being updated (or null if creating)
     */
    public void validateParentCategory(Long parentId, Long currentCategoryId) {
        if (parentId == null) {
            return;
        }

        if (currentCategoryId != null && parentId.equals(currentCategoryId)) {
            throw new BadRequestException("A category cannot be assigned as its own parent");
        }

        Category parent = categoryRepository.findByIdAndDeletedFalse(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with ID: " + parentId));

        if (currentCategoryId != null) {
            // Traversal up the hierarchy to detect cycles
            Category currentAncestor = parent;
            while (currentAncestor != null) {
                if (currentAncestor.getId().equals(currentCategoryId)) {
                    throw new BadRequestException("Circular reference detected: Parent category cannot be a descendant of this category");
                }
                currentAncestor = currentAncestor.getParent();
            }
        }
    }

    /**
     * Validates uniqueness of category name (case-insensitive).
     */
    public void validateNameUniqueness(String name, Long excludeId) {
        String trimmed = name.trim();
        boolean exists = excludeId == null
                ? categoryRepository.existsByNameIgnoreCase(trimmed)
                : categoryRepository.existsByNameIgnoreCaseAndIdNot(trimmed, excludeId);

        if (exists) {
            throw new ConflictException("Category with name '" + trimmed + "' already exists");
        }
    }

    /**
     * Validates uniqueness of category slug (case-insensitive).
     */
    public void validateSlugUniqueness(String slug, Long excludeId) {
        String trimmed = slug.trim().toLowerCase();
        boolean exists = excludeId == null
                ? categoryRepository.existsBySlugIgnoreCase(trimmed)
                : categoryRepository.existsBySlugIgnoreCaseAndIdNot(trimmed, excludeId);

        if (exists) {
            throw new ConflictException("Category with slug '" + trimmed + "' already exists");
        }
    }
}
