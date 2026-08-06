package com.example.ecommerce.storage.service;

import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Strategy interface abstraction over physical storage backends (Local Disk vs AWS S3 / MinIO).
 */
public interface StorageProvider {

    /**
     * Stores byte array content at target key location.
     */
    void store(byte[] bytes, String fileKey, String contentType) throws IOException;

    /**
     * Loads resource stream for key.
     */
    Resource load(String fileKey);

    /**
     * Deletes stored object by key.
     */
    void delete(String fileKey);

    /**
     * Resolves absolute public URL for file key.
     */
    String getPublicUrl(String fileKey);
}
