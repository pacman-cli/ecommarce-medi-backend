package com.example.ecommerce.storage.util;

import com.example.ecommerce.exception.BadRequestException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility for validating file existence, size, extension, MIME types, and path sanitization.
 */
public final class FileValidator {

    public static final long DEFAULT_MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024L; // 10MB

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "webp", "gif"));
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/png", "image/webp", "image/gif", "image/pjpeg", "image/x-png"
    ));

    private static final Pattern SAFE_KEY_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]+/[a-zA-Z0-9_.-]+$");

    private FileValidator() {
    }

    /**
     * Validates an uploaded image file against size, MIME type, and extension requirements.
     */
    public static void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded file must not be null or empty");
        }

        if (file.getSize() > DEFAULT_MAX_FILE_SIZE_BYTES) {
            throw new BadRequestException(String.format("File size (%d bytes) exceeds maximum limit of %d MB",
                    file.getSize(), DEFAULT_MAX_FILE_SIZE_BYTES / (1024 * 1024)));
        }

        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new BadRequestException("Invalid file type. Only JPEG, PNG, WEBP, and GIF images are allowed.");
        }

        String originalFilename = file.getOriginalFilename();
        if (StringUtils.hasText(originalFilename)) {
            String extension = extractExtension(originalFilename);
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new BadRequestException("Invalid file extension: ." + extension + ". Allowed: jpg, jpeg, png, webp, gif");
            }
        }
    }

    /**
     * Generates a unique, sanitized file key (e.g. products/a1b2c3d4_1723456789_image.jpg).
     */
    public static String generateUniqueKey(String folder, String originalFilename) {
        String sanitizedFolder = folder != null ? folder.replaceAll("[^a-zA-Z0-9-]", "").toLowerCase(Locale.ROOT) : "general";
        String extension = extractExtension(originalFilename);

        String baseName = "file";
        if (StringUtils.hasText(originalFilename)) {
            int dot = originalFilename.lastIndexOf('.');
            String nameWithoutExt = dot > 0 ? originalFilename.substring(0, dot) : originalFilename;
            baseName = nameWithoutExt.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase(Locale.ROOT);
            if (baseName.length() > 30) {
                baseName = baseName.substring(0, 30);
            }
        }

        String uuid = UUID.randomUUID().toString().substring(0, 8);
        long timestamp = System.currentTimeMillis();

        return String.format("%s/%s_%d_%s.%s", sanitizedFolder, uuid, timestamp, baseName, extension);
    }

    /**
     * Extracts lowercase extension without dot.
     */
    public static String extractExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "jpg";
        }
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return "jpg";
        }
        return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    /**
     * Sanitizes file keys and verifies absence of path traversal characters (`..`).
     */
    public static void validateKey(String key) {
        if (!StringUtils.hasText(key) || key.contains("..") || key.contains("\\")) {
            throw new BadRequestException("Invalid file storage key: path traversal characters detected");
        }
    }
}
