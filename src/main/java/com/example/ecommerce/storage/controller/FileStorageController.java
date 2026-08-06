package com.example.ecommerce.storage.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.storage.dto.enums.FileCategory;
import com.example.ecommerce.storage.dto.response.FileUploadResponse;
import com.example.ecommerce.storage.dto.response.MultiFileUploadResponse;
import com.example.ecommerce.storage.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * REST controller exposing endpoints for single and bulk file uploads, specialized category media handlers,
 * file downloading, and file deletion.
 */
@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
@Tag(name = "File Storage & Media", description = "Endpoints for uploading, downloading, compressing, and managing image assets across categories")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload single file", description = "Validates, compresses, and stores a single image file under specified category folder")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "File uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid file format, empty payload or file size exceeded")
    })
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @Parameter(description = "Multipart image file payload", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Storage folder category", example = "PRODUCT")
            @RequestParam(value = "category", required = false) FileCategory category) {
        FileUploadResponse response = fileStorageService.storeFile(file, category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "File uploaded successfully"));
    }

    @PostMapping(value = "/upload/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload multiple files in batch", description = "Validates, compresses, and stores a list of image files under specified category folder")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Batch files uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid file payload or empty batch")
    })
    public ResponseEntity<ApiResponse<MultiFileUploadResponse>> uploadFilesBatch(
            @Parameter(description = "List of multipart image files", required = true)
            @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "Storage folder category", example = "PRODUCT")
            @RequestParam(value = "category", required = false) FileCategory category) {
        MultiFileUploadResponse response = fileStorageService.storeFiles(files, category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Batch files uploaded successfully"));
    }

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload profile avatar image", description = "Stores user profile avatar image into 'profiles' folder")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileStorageService.storeProfileImage(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Profile image uploaded successfully"));
    }

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload product media image", description = "Stores product image into 'products' folder")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadProductImage(
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileStorageService.storeProductImage(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Product image uploaded successfully"));
    }

    @PostMapping(value = "/brand", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload brand logo image", description = "Stores brand logo image into 'brands' folder")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadBrandLogo(
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileStorageService.storeBrandLogo(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Brand logo uploaded successfully"));
    }

    @PostMapping(value = "/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload category thumbnail image", description = "Stores category image into 'categories' folder")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadCategoryImage(
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileStorageService.storeCategoryImage(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Category image uploaded successfully"));
    }

    @PostMapping(value = "/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload promotional banner image", description = "Stores banner image into 'banners' folder")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadBannerImage(
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = fileStorageService.storeBannerImage(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Banner image uploaded successfully"));
    }

    @GetMapping("/{folder}/{filename:.+}")
    @Operation(summary = "Download stored file resource", description = "Retrieves and streams stored file by folder and filename key")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File resource retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "File key not found")
    })
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String folder,
            @PathVariable String filename,
            HttpServletRequest request) {
        String fileKey = folder + "/" + filename;
        Resource resource = fileStorageService.loadFileAsResource(fileKey);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Content type determination failure fallback
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{folder}/{filename:.+}")
    @Operation(summary = "Delete stored file", description = "Deletes stored file from physical storage backend using folder and filename key")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File deleted successfully")
    })
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @PathVariable String folder,
            @PathVariable String filename) {
        String fileKey = folder + "/" + filename;
        fileStorageService.deleteFile(fileKey);
        return ResponseEntity.ok(ApiResponse.success(null, "File deleted successfully"));
    }
}
