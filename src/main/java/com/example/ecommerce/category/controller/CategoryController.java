package com.example.ecommerce.category.controller;

import com.example.ecommerce.category.dto.request.CategoryFilterRequest;
import com.example.ecommerce.category.dto.request.CategoryRequest;
import com.example.ecommerce.category.dto.response.CategoryResponse;
import com.example.ecommerce.category.dto.response.CategoryTreeResponse;
import com.example.ecommerce.category.entity.CategoryStatus;
import com.example.ecommerce.category.service.CategoryService;
import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing endpoints for category management, dynamic filtering,
 * hierarchical navigation trees, and CRUD operations.
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Endpoints for managing product categories, hierarchy tree, search, and soft deletion")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Search and filter categories", description = "Returns a paginated list of categories based on dynamic filter criteria")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> getCategories(
            @ModelAttribute CategoryFilterRequest filter,
            @PageableDefault(sort = "sortOrder", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<CategoryResponse> page = categoryService.getCategories(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Categories retrieved successfully"));
    }

    @GetMapping("/tree")
    @Operation(summary = "Get category hierarchy tree", description = "Returns all root categories eagerly loaded with nested child subcategories")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category tree retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<CategoryTreeResponse>>> getCategoryTree() {
        List<CategoryTreeResponse> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(ApiResponse.success(tree, "Category tree retrieved successfully"));
    }

    @GetMapping("/roots")
    @Operation(summary = "Get top-level root categories", description = "Returns list of active categories without a parent category")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Root categories retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getRootCategories() {
        List<CategoryResponse> roots = categoryService.getRootCategories();
        return ResponseEntity.ok(ApiResponse.success(roots, "Root categories retrieved successfully"));
    }

    @GetMapping("/{parentId}/children")
    @Operation(summary = "Get child subcategories", description = "Returns active direct subcategories under the specified parent ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Subcategories retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Parent category not found")
    })
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getSubCategories(
            @Parameter(description = "Parent category ID", required = true) @PathVariable Long parentId) {
        List<CategoryResponse> children = categoryService.getSubCategories(parentId);
        return ResponseEntity.ok(ApiResponse.success(children, "Subcategories retrieved successfully"));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured categories", description = "Returns list of categories marked as featured for homepage/menu displays")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Featured categories retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getFeaturedCategories() {
        List<CategoryResponse> featured = categoryService.getFeaturedCategories();
        return ResponseEntity.ok(ApiResponse.success(featured, "Featured categories retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves category details by unique database identifier")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully"));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get category by URL slug", description = "Retrieves category details by SEO-friendly URL slug")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(
            @Parameter(description = "Category slug", required = true) @PathVariable String slug) {
        CategoryResponse category = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create category", description = "Creates a new category with optional parent, images and SEO metadata (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Category created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload or circular reference"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Category name or slug conflict")
    })
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse created = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Category created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category", description = "Updates category details, images, SEO, or parent hierarchy (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload or circular hierarchy reference"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Category name or slug conflict")
    })
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse updated = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Category updated successfully"));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category status", description = "Toggles category operational status (ACTIVE/INACTIVE) (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<CategoryResponse>> updateStatus(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id,
            @Parameter(description = "New Category Status", required = true) @RequestParam CategoryStatus status) {
        CategoryResponse updated = categoryService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Category status updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete category", description = "Soft deletes a category and recursively deactivates its subcategories (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category soft deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category soft deleted successfully"));
    }
}
