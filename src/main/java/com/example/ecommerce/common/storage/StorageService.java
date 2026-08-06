package com.example.ecommerce.common.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction over the file-storage backend used for uploaded content such as
 * profile images. Kept behind an interface so a cloud provider (e.g. S3) can
 * replace the local filesystem implementation without touching callers.
 */
public interface StorageService {

    /**
     * Persists an uploaded file into a logical folder and returns its key.
     *
     * @param file   the uploaded file
     * @param folder the logical folder (e.g. {@code profiles})
     * @return the storage key used to load the file later
     */
    String store(MultipartFile file, String folder);

    /**
     * Loads a stored file as a resource.
     *
     * @param key the storage key
     * @return the file resource
     */
    Resource load(String key);

    /**
     * Removes a stored file.
     *
     * @param key the storage key
     */
    void delete(String key);
}
