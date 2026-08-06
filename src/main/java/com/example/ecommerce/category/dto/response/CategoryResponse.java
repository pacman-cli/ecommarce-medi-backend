package com.example.ecommerce.category.dto.response;

import com.example.ecommerce.category.entity.CategoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Detailed category response DTO including metadata, images, parent info and children count/list.
 */
@Schema(description = "Category details response")
public class CategoryResponse {

    @Schema(description = "Category ID", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Electronics")
    private String name;

    @Schema(description = "URL slug", example = "electronics")
    private String slug;

    @Schema(description = "Category description", example = "Consumer electronics category")
    private String description;

    @Schema(description = "Category image URL", example = "https://images.example.com/categories/electronics.png")
    private String categoryImage;

    @Schema(description = "Banner image URL", example = "https://images.example.com/banners/electronics-hero.jpg")
    private String bannerImage;

    @Schema(description = "SEO Title", example = "Buy Electronics Online")
    private String seoTitle;

    @Schema(description = "SEO Description", example = "Top deals on electronics")
    private String seoDescription;

    @Schema(description = "Category status", example = "ACTIVE")
    private CategoryStatus status;

    @Schema(description = "Display sort order", example = "0")
    private Integer sortOrder;

    @Schema(description = "Featured indicator", example = "true")
    private boolean featured;

    @Schema(description = "Active indicator", example = "true")
    private boolean active;

    @Schema(description = "Parent category ID", example = "null")
    private Long parentId;

    @Schema(description = "Parent category name", example = "null")
    private String parentName;

    @Schema(description = "Parent category slug", example = "null")
    private String parentSlug;

    @Schema(description = "Child categories count", example = "5")
    private Integer childrenCount;

    @Schema(description = "Sub-categories list", example = "[]")
    private List<CategoryResponse> children;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    @Schema(description = "Last update timestamp")
    private Instant updatedAt;

    @Schema(description = "User who created the record", example = "admin@example.com")
    private String createdBy;

    @Schema(description = "User who last updated the record", example = "admin@example.com")
    private String updatedBy;

    @Schema(description = "Optimistic lock version", example = "0")
    private Long version;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, String slug, String description, String categoryImage,
                            String bannerImage, String seoTitle, String seoDescription, CategoryStatus status,
                            Integer sortOrder, boolean featured, boolean active, Long parentId, String parentName,
                            String parentSlug, Integer childrenCount, List<CategoryResponse> children,
                            Instant createdAt, Instant updatedAt, String createdBy, String updatedBy, Long version) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.categoryImage = categoryImage;
        this.bannerImage = bannerImage;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.status = status;
        this.sortOrder = sortOrder;
        this.featured = featured;
        this.active = active;
        this.parentId = parentId;
        this.parentName = parentName;
        this.parentSlug = parentSlug;
        this.childrenCount = childrenCount;
        this.children = children;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.version = version;
    }

    public static CategoryResponseBuilder builder() {
        return new CategoryResponseBuilder();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentSlug() {
        return parentSlug;
    }

    public void setParentSlug(String parentSlug) {
        this.parentSlug = parentSlug;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public List<CategoryResponse> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryResponse> children) {
        this.children = children;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public static class CategoryResponseBuilder {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private String categoryImage;
        private String bannerImage;
        private String seoTitle;
        private String seoDescription;
        private CategoryStatus status;
        private Integer sortOrder;
        private boolean featured;
        private boolean active;
        private Long parentId;
        private String parentName;
        private String parentSlug;
        private Integer childrenCount;
        private List<CategoryResponse> children;
        private Instant createdAt;
        private Instant updatedAt;
        private String createdBy;
        private String updatedBy;
        private Long version;

        CategoryResponseBuilder() {
        }

        public CategoryResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CategoryResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryResponseBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public CategoryResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CategoryResponseBuilder categoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
            return this;
        }

        public CategoryResponseBuilder bannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
            return this;
        }

        public CategoryResponseBuilder seoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public CategoryResponseBuilder seoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public CategoryResponseBuilder status(CategoryStatus status) {
            this.status = status;
            return this;
        }

        public CategoryResponseBuilder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public CategoryResponseBuilder featured(boolean featured) {
            this.featured = featured;
            return this;
        }

        public CategoryResponseBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public CategoryResponseBuilder parentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public CategoryResponseBuilder parentName(String parentName) {
            this.parentName = parentName;
            return this;
        }

        public CategoryResponseBuilder parentSlug(String parentSlug) {
            this.parentSlug = parentSlug;
            return this;
        }

        public CategoryResponseBuilder childrenCount(Integer childrenCount) {
            this.childrenCount = childrenCount;
            return this;
        }

        public CategoryResponseBuilder children(List<CategoryResponse> children) {
            this.children = children;
            return this;
        }

        public CategoryResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CategoryResponseBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public CategoryResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public CategoryResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public CategoryResponseBuilder version(Long version) {
            this.version = version;
            return this;
        }

        public CategoryResponse build() {
            return new CategoryResponse(id, name, slug, description, categoryImage, bannerImage, seoTitle, seoDescription, status, sortOrder, featured, active, parentId, parentName, parentSlug, childrenCount, children, createdAt, updatedAt, createdBy, updatedBy, version);
        }
    }
}
