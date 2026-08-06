package com.example.ecommerce.category.dto.response;

import com.example.ecommerce.category.entity.CategoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Lightweight tree node response DTO for hierarchical category navigation menus.
 */
@Schema(description = "Category tree hierarchy node")
public class CategoryTreeResponse {

    @Schema(description = "Category ID", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Electronics")
    private String name;

    @Schema(description = "URL slug", example = "electronics")
    private String slug;

    @Schema(description = "Category image URL", example = "https://images.example.com/categories/electronics.png")
    private String categoryImage;

    @Schema(description = "Banner image URL", example = "https://images.example.com/banners/electronics-hero.jpg")
    private String bannerImage;

    @Schema(description = "Sort order", example = "0")
    private Integer sortOrder;

    @Schema(description = "Featured status", example = "true")
    private boolean featured;

    @Schema(description = "Category status", example = "ACTIVE")
    private CategoryStatus status;

    @Schema(description = "Nested child categories")
    private List<CategoryTreeResponse> children;

    public CategoryTreeResponse() {
    }

    public CategoryTreeResponse(Long id, String name, String slug, String categoryImage, String bannerImage,
                                Integer sortOrder, boolean featured, CategoryStatus status, List<CategoryTreeResponse> children) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.categoryImage = categoryImage;
        this.bannerImage = bannerImage;
        this.sortOrder = sortOrder;
        this.featured = featured;
        this.status = status;
        this.children = children;
    }

    public static CategoryTreeResponseBuilder builder() {
        return new CategoryTreeResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public List<CategoryTreeResponse> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryTreeResponse> children) {
        this.children = children;
    }

    public static class CategoryTreeResponseBuilder {
        private Long id;
        private String name;
        private String slug;
        private String categoryImage;
        private String bannerImage;
        private Integer sortOrder;
        private boolean featured;
        private CategoryStatus status;
        private List<CategoryTreeResponse> children;

        CategoryTreeResponseBuilder() {
        }

        public CategoryTreeResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CategoryTreeResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryTreeResponseBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public CategoryTreeResponseBuilder categoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
            return this;
        }

        public CategoryTreeResponseBuilder bannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
            return this;
        }

        public CategoryTreeResponseBuilder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public CategoryTreeResponseBuilder featured(boolean featured) {
            this.featured = featured;
            return this;
        }

        public CategoryTreeResponseBuilder status(CategoryStatus status) {
            this.status = status;
            return this;
        }

        public CategoryTreeResponseBuilder children(List<CategoryTreeResponse> children) {
            this.children = children;
            return this;
        }

        public CategoryTreeResponse build() {
            return new CategoryTreeResponse(id, name, slug, categoryImage, bannerImage, sortOrder, featured, status, children);
        }
    }
}
