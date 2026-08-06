package com.example.ecommerce.storage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Metadata response wrapper for bulk image uploads.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Bulk file upload batch response payload")
public class MultiFileUploadResponse {

    @Schema(description = "List of individual uploaded file metadata items")
    private List<FileUploadResponse> files;

    @Schema(description = "Count of successfully stored files", example = "3")
    private Integer totalUploaded;
}
