package com.example.ecommerce.cache.constant;

/**
 * Constants defining Redis cache region names across the application.
 */
public final class CacheNames {

    private CacheNames() {
    }

    public static final String PRODUCTS = "products";
    public static final String PRODUCT_DETAILS = "product-details";
    public static final String CATEGORIES = "categories";
    public static final String CATEGORY_TREE = "category-tree";
    public static final String BRANDS = "brands";
    public static final String ACTIVE_BRANDS = "active-brands";
    public static final String SEARCH_AUTOCOMPLETE = "search-autocomplete";
    public static final String SEARCH_TRENDING = "search-trending";
    public static final String DASHBOARD_SUMMARY = "dashboard-summary";
}
