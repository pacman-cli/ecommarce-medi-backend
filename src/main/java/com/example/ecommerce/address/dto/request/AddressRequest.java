package com.example.ecommerce.address.dto.request;

import com.example.ecommerce.address.dto.enums.AddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for creating or updating a customer address.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Customer address creation and update request payload")
public class AddressRequest {

    @Schema(description = "Short descriptor label (e.g. HOME, WORK)", example = "HOME")
    @NotBlank(message = "Address label is required")
    @Size(max = 30, message = "Label cannot exceed 30 characters")
    private String label;

    @Schema(description = "Address functional category", example = "SHIPPING")
    @NotNull(message = "Address type must be specified")
    @Builder.Default
    private AddressType addressType = AddressType.SHIPPING;

    @Schema(description = "Recipient full name", example = "Jane Doe")
    @NotBlank(message = "Recipient name is required")
    @Size(max = 120, message = "Recipient name cannot exceed 120 characters")
    private String recipientName;

    @Schema(description = "Contact phone number", example = "+8801700000000")
    @NotBlank(message = "Contact phone is required")
    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    private String phone;

    @Schema(description = "Alternate contact phone", example = "+8801800000000")
    @Size(max = 30, message = "Alternate phone cannot exceed 30 characters")
    private String alternatePhone;

    @Schema(description = "House or flat number details", example = "Flat 4B, House 12, Road 5")
    @Size(max = 60, message = "House number cannot exceed 60 characters")
    private String houseNo;

    @Schema(description = "Street address line", example = "Dhanmondi R/A")
    @NotBlank(message = "Street address is required")
    @Size(max = 120, message = "Street address cannot exceed 120 characters")
    private String street;

    @Schema(description = "Nearby landmark", example = "Near Abahani Playground")
    @Size(max = 120, message = "Landmark cannot exceed 120 characters")
    private String landmark;

    @Schema(description = "Area or neighborhood", example = "Dhanmondi")
    @Size(max = 80, message = "Area cannot exceed 80 characters")
    private String area;

    @Schema(description = "City or Upazila name", example = "Dhaka")
    @NotBlank(message = "City is required")
    @Size(max = 60, message = "City cannot exceed 60 characters")
    private String city;

    @Schema(description = "District name", example = "Dhaka")
    @Size(max = 60, message = "District cannot exceed 60 characters")
    private String district;

    @Schema(description = "Division or Province name", example = "Dhaka Division")
    @Size(max = 60, message = "Division cannot exceed 60 characters")
    private String division;

    @Schema(description = "State or region", example = "Dhaka")
    @Size(max = 60, message = "State cannot exceed 60 characters")
    private String state;

    @Schema(description = "Country name", example = "Bangladesh")
    @NotBlank(message = "Country is required")
    @Size(max = 60, message = "Country cannot exceed 60 characters")
    @Builder.Default
    private String country = "Bangladesh";

    @Schema(description = "Postal or Zip code", example = "1205")
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;

    @Schema(description = "GPS Latitude coordinate", example = "23.7461")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    @Schema(description = "GPS Longitude coordinate", example = "90.3742")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    @Schema(description = "Set as default shipping address", example = "true")
    @Builder.Default
    private Boolean defaultShipping = false;

    @Schema(description = "Set as default billing address", example = "false")
    @Builder.Default
    private Boolean defaultBilling = false;
}
