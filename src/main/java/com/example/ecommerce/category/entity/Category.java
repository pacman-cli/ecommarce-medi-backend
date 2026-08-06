package com.example.ecommerce.category.entity;

import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Product category entity supporting unlimited nesting levels, SEO fields,
 * media images, soft delete and status management.
 */
@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_categories_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_categories_slug", columnNames = "slug")
        }
)
@SQLDelete(sql = "UPDATE categories SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Category extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(length = 1000)
    private String description;

    @Column(name = "category_image", length = 500)
    private String categoryImage;

    @Column(name = "banner_image", length = 500)
    private String bannerImage;

    @Column(name = "seo_title", length = 150)
    private String seoTitle;

    @Column(name = "seo_description", length = 500)
    private String seoDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryStatus status = CategoryStatus.ACTIVE;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private boolean featured = false;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = false)
    @OrderBy("sortOrder ASC, name ASC")
    private List<Category> children = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;

    public Category() {
    }

    public Category(String name, String slug, String description, String categoryImage, String bannerImage,
                    String seoTitle, String seoDescription, CategoryStatus status, Integer sortOrder,
                    boolean featured, boolean deleted, Instant deletedAt, Category parent, List<Category> children, boolean active) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.categoryImage = categoryImage;
        this.bannerImage = bannerImage;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.status = status != null ? status : CategoryStatus.ACTIVE;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
        this.featured = featured;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.parent = parent;
        this.children = children != null ? children : new ArrayList<>();
        this.active = active;
    }

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addChild(Category child) {
        if (child != null) {
            if (this.children == null) {
                this.children = new ArrayList<>();
            }
            this.children.add(child);
            child.setParent(this);
        }
    }

    public void removeChild(Category child) {
        if (child != null && this.children != null) {
            this.children.remove(child);
            child.setParent(null);
        }
    }

    public static class CategoryBuilder {
        private String name;
        private String slug;
        private String description;
        private String categoryImage;
        private String bannerImage;
        private String seoTitle;
        private String seoDescription;
        private CategoryStatus status = CategoryStatus.ACTIVE;
        private Integer sortOrder = 0;
        private boolean featured = false;
        private boolean deleted = false;
        private Instant deletedAt;
        private Category parent;
        private List<Category> children = new ArrayList<>();
        private boolean active = true;

        CategoryBuilder() {
        }

        public CategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public CategoryBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CategoryBuilder categoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
            return this;
        }

        public CategoryBuilder bannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
            return this;
        }

        public CategoryBuilder seoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public CategoryBuilder seoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public CategoryBuilder status(CategoryStatus status) {
            this.status = status;
            return this;
        }

        public CategoryBuilder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public CategoryBuilder featured(boolean featured) {
            this.featured = featured;
            return this;
        }

        public CategoryBuilder deleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public CategoryBuilder deletedAt(Instant deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public CategoryBuilder parent(Category parent) {
            this.parent = parent;
            return this;
        }

        public CategoryBuilder children(List<Category> children) {
            this.children = children;
            return this;
        }

        public CategoryBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Category build() {
            return new Category(name, slug, description, categoryImage, bannerImage, seoTitle, seoDescription, status, sortOrder, featured, deleted, deletedAt, parent, children, active);
        }
    }
}
