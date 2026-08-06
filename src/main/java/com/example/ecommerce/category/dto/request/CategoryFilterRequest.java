package com.example.ecommerce.category.dto.request;

import com.example.ecommerce.category.entity.CategoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Filter parameters for dynamic category searching and filtering.
 */
@Schema(description = "Category filter criteria for search and pagination")
public class CategoryFilterRequest {

    @Schema(description = "Search keyword matching name, slug or description", example = "phone")
    private String search;

    @Schema(description = "Exact or partial name filter", example = "Electronics")
    private String name;

    @Schema(description = "Exact slug filter", example = "electronics")
    private String slug;

    @Schema(description = "Filter by status", example = "ACTIVE")
    private CategoryStatus status;

    @Schema(description = "Filter by parent category ID", example = "1")
    private Long parentId;

    @Schema(description = "If true, restricts results to top-level root categories", example = "false")
    private Boolean rootOnly;

    @Schema(description = "Filter by featured flag", example = "true")
    private Boolean featured;

    @Schema(description = "Include inactive categories (Admin default or opt-in)", example = "false")
    private Boolean includeInactive;

    public CategoryFilterRequest() {
    }

    public CategoryFilterRequest(String search, String name, String slug, CategoryStatus status, Long parentId, Boolean rootOnly, Boolean featured, Boolean includeInactive) {
        this.search = search;
        this.name = name;
        this.slug = slug;
        this.status = status;
        this.parentId = parentId;
        this.rootOnly = rootOnly;
        this.featured = featured;
        this.includeInactive = includeInactive;
    }

    public static CategoryFilterRequestBuilder builder() {
        return new CategoryFilterRequestBuilder();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getRootOnly() {
        return rootOnly;
    }

    public void setRootOnly(Boolean rootOnly) {
        this.rootOnly = rootOnly;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getIncludeInactive() {
        return includeInactive;
    }

    public void setIncludeInactive(Boolean includeInactive) {
        this.includeInactive = includeInactive;
    }

    public static class CategoryFilterRequestBuilder {
        private String search;
        private String name;
        private String slug;
        private CategoryStatus status;
        private Long parentId;
        private Boolean rootOnly;
        private Boolean featured;
        private Boolean includeInactive;

        CategoryFilterRequestBuilder() {
        }

        public CategoryFilterRequestBuilder search(String search) {
            this.search = search;
            return this;
        }

        public CategoryFilterRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryFilterRequestBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public CategoryFilterRequestBuilder status(CategoryStatus status) {
            this.status = status;
            return this;
        }

        public CategoryFilterRequestBuilder parentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public CategoryFilterRequestBuilder rootOnly(Boolean rootOnly) {
            this.rootOnly = rootOnly;
            return this;
        }

        public CategoryFilterRequestBuilder featured(Boolean featured) {
            this.featured = featured;
            return this;
        }

        public CategoryFilterRequestBuilder includeInactive(Boolean includeInactive) {
            this.includeInactive = includeInactive;
            return this;
        }

        public CategoryFilterRequest build() {
            return new CategoryFilterRequest(search, name, slug, status, parentId, rootOnly, featured, includeInactive);
        }
    }
}
