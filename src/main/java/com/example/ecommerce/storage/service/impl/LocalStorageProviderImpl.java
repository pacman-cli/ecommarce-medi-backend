package com.example.ecommerce.storage.service.impl;

import com.example.ecommerce.config.properties.StorageProperties;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.storage.service.StorageProvider;
import com.example.ecommerce.storage.util.FileValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Local filesystem implementation of {@link StorageProvider}.
 */
@Slf4j
@Primary
@Service("localStorageProvider")
@RequiredArgsConstructor
public class LocalStorageProviderImpl implements StorageProvider {

    private final StorageProperties properties;
    private Path rootDir;

    @PostConstruct
    public void init() {
        this.rootDir = properties.getUploadDir().toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize local upload root directory: " + rootDir, e);
        }
    }

    @Override
    public void store(byte[] bytes, String fileKey, String contentType) throws IOException {
        FileValidator.validateKey(fileKey);
        Path targetPath = safeResolve(fileKey);
        Files.createDirectories(targetPath.getParent());
        Files.write(targetPath, bytes);
        log.debug("Stored {} bytes at path: {}", bytes.length, targetPath);
    }

    @Override
    public Resource load(String fileKey) {
        FileValidator.validateKey(fileKey);
        Path path = safeResolve(fileKey);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new ResourceNotFoundException("File", "key", fileKey);
        }
        return new FileSystemResource(path);
    }

    @Override
    public void delete(String fileKey) {
        FileValidator.validateKey(fileKey);
        try {
            Path path = safeResolve(fileKey);
            Files.deleteIfExists(path);
            log.debug("Deleted file at path: {}", path);
        } catch (IOException e) {
            log.warn("Failed to delete file key {}: {}", fileKey, e.getMessage());
        }
    }

    @Override
    public String getPublicUrl(String fileKey) {
        String baseUrl = properties.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl + "/storage/" + fileKey;
    }

    private Path safeResolve(String key) {
        Path resolved = rootDir.resolve(key).normalize();
        if (!resolved.startsWith(rootDir)) {
            throw new BadRequestException("Invalid file storage key: attempted path traversal outside upload root");
        }
        return resolved;
    }
}
