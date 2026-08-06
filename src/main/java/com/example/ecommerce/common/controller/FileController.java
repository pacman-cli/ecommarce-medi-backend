package com.example.ecommerce.common.controller;

import com.example.ecommerce.common.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Serves stored files (e.g. profile images) by their storage key.
 */
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "Serve stored files")
public class FileController {

    private final StorageService storageService;

    /**
     * Streams a stored file identified by its key.
     *
     * @param key the storage key
     * @return the file resource
     */
    @GetMapping("/{key:.+}")
    @Operation(summary = "Fetch a stored file by key")
    public ResponseEntity<Resource> getFile(@PathVariable String key) {
        Resource resource = storageService.load(key);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
