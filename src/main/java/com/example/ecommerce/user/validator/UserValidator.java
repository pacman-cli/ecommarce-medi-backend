package com.example.ecommerce.user.validator;

import com.example.ecommerce.exception.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * Business validation for user-profile operations that cannot be expressed with
 * Jakarta Validation annotations.
 */
@Component
public class UserValidator {

    private static final long MAX_IMAGE_BYTES = 2L * 1024 * 1024;
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    /**
     * Validates a profile image upload (presence, MIME type and size).
     *
     * @param file the uploaded file
     */
    public void validateProfileImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Profile image is required");
        }
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("Only JPEG, PNG or WEBP images are allowed");
        }
        if (file.getSize() > MAX_IMAGE_BYTES) {
            throw new BadRequestException("Profile image must not exceed 2 MB");
        }
    }
}
