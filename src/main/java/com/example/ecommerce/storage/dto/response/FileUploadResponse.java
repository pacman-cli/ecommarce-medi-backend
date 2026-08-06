package com.example.ecommerce.storage.dto.response;

import com.example.ecommerce.storage.dto.enums.FileCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Metadata DTO returned upon successful file storage.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Uploaded file metadata payload")
public class FileUploadResponse {

    @Schema(description = "Unique storage file key identifier", example = "products/a1b2c3d4-product-image.jpg")
    private String fileKey;

    @Schema(description = "Publicly accessible file download URL", example = "http://localhost:8080/api/v1/storage/products/a1b2c3d4-product-image.jpg")
    private String fileUrl;

    @Schema(description = "Original filename submitted by user", example = "product-image.jpg")
    private String originalFileName;

    @Schema(description = "MIME content type", example = "image/jpeg")
    private String contentType;

    @Schema(description = "File size in bytes", example = "1048576")
    private Long sizeBytes;

    @Schema(description = "Media asset folder category", example = "PRODUCT")
    private FileCategory category;

    @Schema(description = "Timestamp when uploaded", example = "2026-08-05T14:00:00Z")
    private Instant uploadedAt;
}
