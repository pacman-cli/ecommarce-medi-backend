package com.example.ecommerce.delivery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for creating or updating a delivery partner carrier.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delivery partner creation and update payload")
public class DeliveryPartnerRequest {

    @Schema(description = "Partner carrier name", example = "Steadfast Courier")
    @NotBlank(message = "Partner name is required")
    private String name;

    @Schema(description = "Unique partner code identifier", example = "STEADFAST")
    @NotBlank(message = "Partner code is required")
    private String code;

    @Schema(description = "Contact phone", example = "+8801700000000")
    private String contactPhone;

    @Schema(description = "Contact email", example = "support@steadfast.com.bd")
    private String contactEmail;

    @Schema(description = "API endpoint URL", example = "https://steadfast.com.bd/api/v1")
    private String apiEndpoint;

    @Schema(description = "API secret key", example = "stdf_secret_key_12345")
    private String apiKey;

    @Schema(description = "Is Cash On Delivery supported", example = "true")
    @Builder.Default
    private Boolean codSupported = true;

    @Schema(description = "Is carrier active", example = "true")
    @Builder.Default
    private Boolean active = true;
}
