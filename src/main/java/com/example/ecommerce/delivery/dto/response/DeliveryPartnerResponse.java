package com.example.ecommerce.delivery.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO representing a logistics delivery partner carrier.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delivery partner carrier payload")
public class DeliveryPartnerResponse {

    @Schema(description = "Partner ID", example = "1")
    private Long id;

    @Schema(description = "Partner name", example = "Steadfast Courier")
    private String name;

    @Schema(description = "Partner code", example = "STEADFAST")
    private String code;

    @Schema(description = "Contact phone", example = "+8801700000000")
    private String contactPhone;

    @Schema(description = "Contact email", example = "support@steadfast.com.bd")
    private String contactEmail;

    @Schema(description = "API endpoint", example = "https://steadfast.com.bd/api/v1")
    private String apiEndpoint;

    @Schema(description = "Is Cash On Delivery supported", example = "true")
    private boolean codSupported;

    @Schema(description = "Is partner active", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp", example = "2026-08-05T14:00:00Z")
    private Instant createdAt;
}
