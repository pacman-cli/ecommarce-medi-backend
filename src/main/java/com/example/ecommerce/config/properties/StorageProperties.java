package com.example.ecommerce.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

/**
 * Local file-storage configuration bound from the {@code app.storage.*} namespace.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    /** Directory where uploaded files are persisted. */
    private Path uploadDir = Path.of("./uploads");

    /** Public base URL used to build absolute file URLs returned by the API. */
    private String baseUrl = "http://localhost:8080/api/v1";
}
