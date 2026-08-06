package com.example.ecommerce.storage.service;

import com.example.ecommerce.storage.dto.enums.FileCategory;
import com.example.ecommerce.storage.dto.response.FileUploadResponse;
import com.example.ecommerce.storage.dto.response.MultiFileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for handling file uploads, image compression, media asset categorization,
 * downloading, and deletion operations.
 */
public interface FileStorageService {

    /**
     * Validates, compresses, and stores a single file under a specified category folder.
     */
    FileUploadResponse storeFile(MultipartFile file, FileCategory category);

    /**
     * Validates, compresses, and stores multiple files under a specified category folder.
     */
    MultiFileUploadResponse storeFiles(List<MultipartFile> files, FileCategory category);

    /**
     * Specialized helper for user profile image uploads.
     */
    FileUploadResponse storeProfileImage(MultipartFile file);

    /**
     * Specialized helper for catalogue product image uploads.
     */
    FileUploadResponse storeProductImage(MultipartFile file);

    /**
     * Specialized helper for brand logo uploads.
     */
    FileUploadResponse storeBrandLogo(MultipartFile file);

    /**
     * Specialized helper for category thumbnail uploads.
     */
    FileUploadResponse storeCategoryImage(MultipartFile file);

    /**
     * Specialized helper for promotional banner uploads.
     */
    FileUploadResponse storeBannerImage(MultipartFile file);

    /**
     * Loads stored file as a downloadable Spring {@link Resource}.
     */
    Resource loadFileAsResource(String fileKey);

    /**
     * Deletes stored file by key.
     */
    void deleteFile(String fileKey);

    /**
     * Returns absolute public download URL for storage key.
     */
    String getPublicFileUrl(String fileKey);
}
