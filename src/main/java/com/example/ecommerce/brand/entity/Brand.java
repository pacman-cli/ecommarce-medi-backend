package com.example.ecommerce.brand.entity;

import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

/**
 * Brand entity supporting logo, banner images, website link, country, SEO,
 * status management and soft deletion.
 */
@Entity
@Table(
        name = "brands",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brands_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_brands_slug", columnNames = "slug")
        }
)
@SQLDelete(sql = "UPDATE brands SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Brand extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String logo;

    @Column(name = "banner_image", length = 500)
    private String bannerImage;

    @Column(name = "website_url", length = 300)
    private String websiteUrl;

    @Column(length = 100)
    private String country;

    @Column(name = "seo_title", length = 150)
    private String seoTitle;

    @Column(name = "seo_description", length = 500)
    private String seoDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BrandStatus status = BrandStatus.ACTIVE;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private boolean featured = false;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(nullable = false)
    private boolean active = true;

    public Brand() {
    }

    public Brand(String name, String slug, String description, String logo, String bannerImage, String websiteUrl,
                 String country, String seoTitle, String seoDescription, BrandStatus status, Integer sortOrder,
                 boolean featured, boolean deleted, Instant deletedAt, boolean active) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.logo = logo;
        this.bannerImage = bannerImage;
        this.websiteUrl = websiteUrl;
        this.country = country;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.status = status != null ? status : BrandStatus.ACTIVE;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
        this.featured = featured;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.active = active;
    }

    public static BrandBuilder builder() {
        return new BrandBuilder();
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public BrandStatus getStatus() {
        return status;
    }

    public void setStatus(BrandStatus status) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static class BrandBuilder {
        private String name;
        private String slug;
        private String description;
        private String logo;
        private String bannerImage;
        private String websiteUrl;
        private String country;
        private String seoTitle;
        private String seoDescription;
        private BrandStatus status = BrandStatus.ACTIVE;
        private Integer sortOrder = 0;
        private boolean featured = false;
        private boolean deleted = false;
        private Instant deletedAt;
        private boolean active = true;

        BrandBuilder() {
        }

        public BrandBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BrandBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public BrandBuilder description(String description) {
            this.description = description;
            return this;
        }

        public BrandBuilder logo(String logo) {
            this.logo = logo;
            return this;
        }

        public BrandBuilder bannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
            return this;
        }

        public BrandBuilder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public BrandBuilder country(String country) {
            this.country = country;
            return this;
        }

        public BrandBuilder seoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public BrandBuilder seoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public BrandBuilder status(BrandStatus status) {
            this.status = status;
            return this;
        }

        public BrandBuilder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public BrandBuilder featured(boolean featured) {
            this.featured = featured;
            return this;
        }

        public BrandBuilder deleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public BrandBuilder deletedAt(Instant deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public BrandBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Brand build() {
            return new Brand(name, slug, description, logo, bannerImage, websiteUrl, country, seoTitle, seoDescription, status, sortOrder, featured, deleted, deletedAt, active);
        }
    }
}
