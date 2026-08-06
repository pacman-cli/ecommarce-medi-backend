package com.example.ecommerce.storage.service.impl;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.storage.dto.enums.FileCategory;
import com.example.ecommerce.storage.dto.response.FileUploadResponse;
import com.example.ecommerce.storage.dto.response.MultiFileUploadResponse;
import com.example.ecommerce.storage.service.FileStorageService;
import com.example.ecommerce.storage.service.StorageProvider;
import com.example.ecommerce.storage.util.FileValidator;
import com.example.ecommerce.storage.util.ImageCompressor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for file storage, image validation, compression,
 * category folder routing, and provider delegate invocation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final StorageProvider storageProvider;

    @Override
    public FileUploadResponse storeFile(MultipartFile file, FileCategory category) {
        FileCategory targetCategory = category != null ? category : FileCategory.GENERAL;
        FileValidator.validateImageFile(file);

        String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElse("image.jpg");
        String fileKey = FileValidator.generateUniqueKey(targetCategory.getFolderName(), originalFilename);

        try {
            byte[] compressedBytes = ImageCompressor.compressImage(file);
            String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

            storageProvider.store(compressedBytes, fileKey, contentType);

            String publicUrl = storageProvider.getPublicUrl(fileKey);

            log.info("Successfully stored asset under key: {}", fileKey);

            return FileUploadResponse.builder()
                    .fileKey(fileKey)
                    .fileUrl(publicUrl)
                    .originalFileName(originalFilename)
                    .contentType(contentType)
                    .sizeBytes((long) compressedBytes.length)
                    .category(targetCategory)
                    .uploadedAt(Instant.now())
                    .build();

        } catch (IOException e) {
            log.error("Failed to process and store file key {}: {}", fileKey, e.getMessage(), e);
            throw new BadRequestException("Failed to process and store image file: " + e.getMessage());
        }
    }

    @Override
    public MultiFileUploadResponse storeFiles(List<MultipartFile> files, FileCategory category) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestException("File batch list must not be null or empty");
        }

        List<FileUploadResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                responses.add(storeFile(file, category));
            }
        }

        return MultiFileUploadResponse.builder()
                .files(responses)
                .totalUploaded(responses.size())
                .build();
    }

    @Override
    public FileUploadResponse storeProfileImage(MultipartFile file) {
        return storeFile(file, FileCategory.PROFILE);
    }

    @Override
    public FileUploadResponse storeProductImage(MultipartFile file) {
        return storeFile(file, FileCategory.PRODUCT);
    }

    @Override
    public FileUploadResponse storeBrandLogo(MultipartFile file) {
        return storeFile(file, FileCategory.BRAND);
    }

    @Override
    public FileUploadResponse storeCategoryImage(MultipartFile file) {
        return storeFile(file, FileCategory.CATEGORY);
    }

    @Override
    public FileUploadResponse storeBannerImage(MultipartFile file) {
        return storeFile(file, FileCategory.BANNER);
    }

    @Override
    public Resource loadFileAsResource(String fileKey) {
        return storageProvider.load(fileKey);
    }

    @Override
    public void deleteFile(String fileKey) {
        storageProvider.delete(fileKey);
    }

    @Override
    public String getPublicFileUrl(String fileKey) {
        return storageProvider.getPublicUrl(fileKey);
    }
}
