package com.example.ecommerce.storage.service;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.storage.dto.enums.FileCategory;
import com.example.ecommerce.storage.dto.response.FileUploadResponse;
import com.example.ecommerce.storage.dto.response.MultiFileUploadResponse;
import com.example.ecommerce.storage.service.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceImplTest {

    @Mock
    private StorageProvider storageProvider;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    private MockMultipartFile validFile;

    @BeforeEach
    void setUp() {
        validFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test-image-content".getBytes()
        );
    }

    @Test
    void testStoreFileSuccess() throws IOException {
        when(storageProvider.getPublicUrl(anyString())).thenReturn("http://localhost:8080/api/v1/storage/products/key.jpg");

        FileUploadResponse response = fileStorageService.storeFile(validFile, FileCategory.PRODUCT);

        assertNotNull(response);
        assertEquals("test-image.jpg", response.getOriginalFileName());
        assertEquals(FileCategory.PRODUCT, response.getCategory());
        verify(storageProvider, times(1)).store(any(byte[].class), anyString(), eq("image/jpeg"));
    }

    @Test
    void testStoreInvalidFileTypeThrowsException() {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "script.exe",
                "application/x-msdownload",
                "binary".getBytes()
        );

        assertThrows(BadRequestException.class, () -> fileStorageService.storeFile(invalidFile, FileCategory.GENERAL));
    }

    @Test
    void testStoreFilesBatch() throws IOException {
        when(storageProvider.getPublicUrl(anyString())).thenReturn("http://localhost:8080/api/v1/storage/products/key.jpg");

        MockMultipartFile file2 = new MockMultipartFile("files", "test2.png", "image/png", "content2".getBytes());
        MultiFileUploadResponse response = fileStorageService.storeFiles(Arrays.asList(validFile, file2), FileCategory.PRODUCT);

        assertNotNull(response);
        assertEquals(2, response.getTotalUploaded());
    }

    @Test
    void testDeleteFile() {
        fileStorageService.deleteFile("products/test-key.jpg");
        verify(storageProvider, times(1)).delete(eq("products/test-key.jpg"));
    }
}
