package com.example.ecommerce.delivery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for dispatching an assigned delivery rider to a shipment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rider assignment request payload")
public class AssignRiderRequest {

    @Schema(description = "Rider full name", example = "Rahim Uddin")
    @NotBlank(message = "Rider name is required")
    private String riderName;

    @Schema(description = "Rider phone number", example = "+8801711223344")
    @NotBlank(message = "Rider phone is required")
    private String riderPhone;

    @Schema(description = "Vehicle description and registration number", example = "Motorbike (Dhaka Metro-HA-1234)")
    private String vehicleInfo;
}
