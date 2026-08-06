package com.example.ecommerce.brand.dto.response;

import com.example.ecommerce.brand.entity.BrandStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Detailed brand response DTO.
 */
@Schema(description = "Brand details response")
public class BrandResponse {

    @Schema(description = "Brand ID", example = "1")
    private Long id;

    @Schema(description = "Brand display name", example = "Apple")
    private String name;

    @Schema(description = "URL slug", example = "apple")
    private String slug;

    @Schema(description = "Brand description", example = "Global technology leader")
    private String description;

    @Schema(description = "Logo image URL", example = "https://images.example.com/brands/apple-logo.png")
    private String logo;

    @Schema(description = "Banner image URL", example = "https://images.example.com/brands/apple-banner.jpg")
    private String bannerImage;

    @Schema(description = "Official website URL", example = "https://www.apple.com")
    private String websiteUrl;

    @Schema(description = "Country of origin", example = "United States")
    private String country;

    @Schema(description = "SEO Title", example = "Buy Apple Products Online")
    private String seoTitle;

    @Schema(description = "SEO Description", example = "Shop top rated Apple devices")
    private String seoDescription;

    @Schema(description = "Operational status", example = "ACTIVE")
    private BrandStatus status;

    @Schema(description = "Sort order", example = "0")
    private Integer sortOrder;

    @Schema(description = "Featured brand indicator", example = "true")
    private boolean featured;

    @Schema(description = "Active indicator", example = "true")
    private boolean active;

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

    public BrandResponse() {
    }

    public BrandResponse(Long id, String name, String slug, String description, String logo, String bannerImage,
                         String websiteUrl, String country, String seoTitle, String seoDescription, BrandStatus status,
                         Integer sortOrder, boolean featured, boolean active, Instant createdAt, Instant updatedAt,
                         String createdBy, String updatedBy, Long version) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.logo = logo;
        this.bannerImage = bannerImage;
        this.websiteUrl = websiteUrl;
        this.country = country;
        this.seoTitle = seoTitle;
        this.seoDescription = seoDescription;
        this.status = status;
        this.sortOrder = sortOrder;
        this.featured = featured;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.version = version;
    }

    public static BrandResponseBuilder builder() {
        return new BrandResponseBuilder();
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public static class BrandResponseBuilder {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private String logo;
        private String bannerImage;
        private String websiteUrl;
        private String country;
        private String seoTitle;
        private String seoDescription;
        private BrandStatus status;
        private Integer sortOrder;
        private boolean featured;
        private boolean active;
        private Instant createdAt;
        private Instant updatedAt;
        private String createdBy;
        private String updatedBy;
        private Long version;

        BrandResponseBuilder() {
        }

        public BrandResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BrandResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BrandResponseBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public BrandResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public BrandResponseBuilder logo(String logo) {
            this.logo = logo;
            return this;
        }

        public BrandResponseBuilder bannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
            return this;
        }

        public BrandResponseBuilder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public BrandResponseBuilder country(String country) {
            this.country = country;
            return this;
        }

        public BrandResponseBuilder seoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public BrandResponseBuilder seoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public BrandResponseBuilder status(BrandStatus status) {
            this.status = status;
            return this;
        }

        public BrandResponseBuilder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public BrandResponseBuilder featured(boolean featured) {
            this.featured = featured;
            return this;
        }

        public BrandResponseBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public BrandResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BrandResponseBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BrandResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public BrandResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public BrandResponseBuilder version(Long version) {
            this.version = version;
            return this;
        }

        public BrandResponse build() {
            return new BrandResponse(id, name, slug, description, logo, bannerImage, websiteUrl, country, seoTitle, seoDescription, status, sortOrder, featured, active, createdAt, updatedAt, createdBy, updatedBy, version);
        }
    }
}
