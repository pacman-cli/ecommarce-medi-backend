package com.example.ecommerce.brand.dto.request;

import com.example.ecommerce.brand.entity.BrandStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Filter parameters for dynamic searching, filtering and pagination of brands.
 */
@Schema(description = "Brand filter criteria for search and pagination")
public class BrandFilterRequest {

    @Schema(description = "Search keyword matching name, slug, description or country", example = "Apple")
    private String search;

    @Schema(description = "Exact or partial name filter", example = "Apple")
    private String name;

    @Schema(description = "Exact slug filter", example = "apple")
    private String slug;

    @Schema(description = "Filter by country", example = "United States")
    private String country;

    @Schema(description = "Filter by operational status", example = "ACTIVE")
    private BrandStatus status;

    @Schema(description = "Filter by featured indicator", example = "true")
    private Boolean featured;

    @Schema(description = "Include inactive brands (Admin default or opt-in)", example = "false")
    private Boolean includeInactive;

    public BrandFilterRequest() {
    }

    public BrandFilterRequest(String search, String name, String slug, String country, BrandStatus status, Boolean featured, Boolean includeInactive) {
        this.search = search;
        this.name = name;
        this.slug = slug;
        this.country = country;
        this.status = status;
        this.featured = featured;
        this.includeInactive = includeInactive;
    }

    public static BrandFilterRequestBuilder builder() {
        return new BrandFilterRequestBuilder();
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BrandStatus getStatus() {
        return status;
    }

    public void setStatus(BrandStatus status) {
        this.status = status;
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

    public static class BrandFilterRequestBuilder {
        private String search;
        private String name;
        private String slug;
        private String country;
        private BrandStatus status;
        private Boolean featured;
        private Boolean includeInactive;

        BrandFilterRequestBuilder() {
        }

        public BrandFilterRequestBuilder search(String search) {
            this.search = search;
            return this;
        }

        public BrandFilterRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BrandFilterRequestBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public BrandFilterRequestBuilder country(String country) {
            this.country = country;
            return this;
        }

        public BrandFilterRequestBuilder status(BrandStatus status) {
            this.status = status;
            return this;
        }

        public BrandFilterRequestBuilder featured(Boolean featured) {
            this.featured = featured;
            return this;
        }

        public BrandFilterRequestBuilder includeInactive(Boolean includeInactive) {
            this.includeInactive = includeInactive;
            return this;
        }

        public BrandFilterRequest build() {
            return new BrandFilterRequest(search, name, slug, country, status, featured, includeInactive);
        }
    }
}
