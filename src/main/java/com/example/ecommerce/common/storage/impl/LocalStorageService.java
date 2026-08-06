package com.example.ecommerce.common.storage.impl;

import com.example.ecommerce.common.storage.StorageService;
import com.example.ecommerce.config.properties.StorageProperties;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Filesystem-backed {@link StorageService}.
 *
 * <p>Files are stored under a random UUID key inside a logical folder. All key
 * resolution is normalised and verified to stay within the configured root
 * directory, preventing path-traversal attacks.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LocalStorageService implements StorageService {

    private static final Pattern SAFE_FOLDER = Pattern.compile("[a-z0-9-]+");

    private final StorageProperties properties;
    private Path root;

    /**
     * Initialises the root upload directory.
     */
    @PostConstruct
    public void init() {
        this.root = properties.getUploadDir().toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not create upload directory: " + root, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required and must not be empty");
        }
        if (!SAFE_FOLDER.matcher(folder).matches()) {
            throw new BadRequestException("Invalid storage folder: " + folder);
        }
        String originalName = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
        String extension = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) {
            extension = originalName.substring(dot).toLowerCase(Locale.ROOT);
        }
        String key = folder + "/" + UUID.randomUUID() + extension;
        Path target = safeResolve(root, key);
        try {
            Files.createDirectories(target.getParent());
            file.transferTo(target);
        } catch (IOException ex) {
            throw new BadRequestException("Failed to store file: " + ex.getMessage(), ex);
        }
        log.debug("Stored file: {}", key);
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource load(String key) {
        Path path = safeResolve(root, key);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new ResourceNotFoundException("File", "key", key);
        }
        return new FileSystemResource(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(safeResolve(root, key));
            log.debug("Deleted file: {}", key);
        } catch (IOException ex) {
            log.warn("Could not delete file {}: {}", key, ex.getMessage());
        }
    }

    /**
     * Resolves a key against the root directory and refuses paths escaping it.
     *
     * @param base the root directory
     * @param key  the storage key
     * @return the normalised path within the root
     */
    private Path safeResolve(Path base, String key) {
        Path resolved = base.resolve(key).normalize();
        if (!resolved.startsWith(base)) {
            throw new BadRequestException("Invalid file key: " + key);
        }
        return resolved;
    }
}
