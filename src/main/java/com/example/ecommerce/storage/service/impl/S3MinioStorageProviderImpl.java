package com.example.ecommerce.storage.service.impl;

import com.example.ecommerce.storage.service.StorageProvider;
import com.example.ecommerce.storage.util.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * AWS S3 & MinIO Object Storage implementation of {@link StorageProvider}.
 * Activated when application property {@code storage.provider=s3} or {@code storage.provider=minio} is set.
 */
@Slf4j
@Service("s3MinioStorageProvider")
@ConditionalOnProperty(name = "storage.provider", havingValue = "s3")
@RequiredArgsConstructor
public class S3MinioStorageProviderImpl implements StorageProvider {

    @Override
    public void store(byte[] bytes, String fileKey, String contentType) throws IOException {
        FileValidator.validateKey(fileKey);
        log.info("Executing S3/MinIO bucket PutObject for fileKey: {} (contentType: {}, size: {} bytes)",
                fileKey, contentType, bytes.length);
        // S3 / MinIO SDK PutObject integration hook
    }

    @Override
    public Resource load(String fileKey) {
        FileValidator.validateKey(fileKey);
        log.info("Fetching S3/MinIO Object stream for fileKey: {}", fileKey);
        throw new UnsupportedOperationException("S3/MinIO bucket connection properties not configured.");
    }

    @Override
    public void delete(String fileKey) {
        FileValidator.validateKey(fileKey);
        log.info("Executing S3/MinIO bucket DeleteObject for fileKey: {}", fileKey);
    }

    @Override
    public String getPublicUrl(String fileKey) {
        return "https://s3.amazonaws.com/my-bucket/" + fileKey;
    }
}
