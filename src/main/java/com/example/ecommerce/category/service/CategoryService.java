package com.example.ecommerce.category.service;

import com.example.ecommerce.category.dto.request.CategoryFilterRequest;
import com.example.ecommerce.category.dto.request.CategoryRequest;
import com.example.ecommerce.category.dto.response.CategoryResponse;
import com.example.ecommerce.category.dto.response.CategoryTreeResponse;
import com.example.ecommerce.category.entity.CategoryStatus;
import com.example.ecommerce.common.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface defining business operations for category management.
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param request creation request payload
     * @return created category response DTO
     */
    CategoryResponse createCategory(CategoryRequest request);

    /**
     * Updates an existing category by ID.
     *
     * @param id      category ID
     * @param request update payload
     * @return updated category response DTO
     */
    CategoryResponse updateCategory(Long id, CategoryRequest request);

    /**
     * Retrieves category by ID.
     *
     * @param id category ID
     * @return category response DTO
     */
    CategoryResponse getCategoryById(Long id);

    /**
     * Retrieves category by URL slug.
     *
     * @param slug category slug
     * @return category response DTO
     */
    CategoryResponse getCategoryBySlug(String slug);

    /**
     * Searches categories with dynamic filtering and pagination.
     *
     * @param filter   filter criteria
     * @param pageable pagination parameters
     * @return paginated category response
     */
    PageResponse<CategoryResponse> getCategories(CategoryFilterRequest filter, Pageable pageable);

    /**
     * Retrieves full category hierarchy tree.
     *
     * @return list of root categories with nested subcategories
     */
    List<CategoryTreeResponse> getCategoryTree();

    /**
     * Returns list of active top-level root categories.
     *
     * @return root categories list
     */
    List<CategoryResponse> getRootCategories();

    /**
     * Returns list of direct active child categories under a parent.
     *
     * @param parentId parent category ID
     * @return subcategories list
     */
    List<CategoryResponse> getSubCategories(Long parentId);

    /**
     * Returns list of featured categories.
     *
     * @return featured categories list
     */
    List<CategoryResponse> getFeaturedCategories();

    /**
     * Updates category operational status (ACTIVE/INACTIVE).
     *
     * @param id     category ID
     * @param status new category status
     * @return updated category response DTO
     */
    CategoryResponse updateStatus(Long id, CategoryStatus status);

    /**
     * Soft deletes category and its soft-deleted subcategories.
     *
     * @param id category ID
     */
    void deleteCategory(Long id);
}
