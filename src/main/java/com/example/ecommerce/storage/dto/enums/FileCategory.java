package com.example.ecommerce.storage.dto.enums;

/**
 * Storage folder categories for organizing uploaded media assets.
 */
public enum FileCategory {
    PROFILE("profiles"),
    PRODUCT("products"),
    BRAND("brands"),
    CATEGORY("categories"),
    BANNER("banners"),
    GENERAL("general");

    private final String folderName;

    FileCategory(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}
