package com.example.ecommerce.address.dto.response;

import com.example.ecommerce.address.dto.enums.AddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Customer address response DTO exposing full geography and GPS coordinate details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Customer address details payload")
public class AddressResponse {

    @Schema(description = "Address ID", example = "101")
    private Long id;

    @Schema(description = "Customer User ID owner", example = "5")
    private Long userId;

    @Schema(description = "Descriptor label", example = "HOME")
    private String label;

    @Schema(description = "Address functional type", example = "SHIPPING")
    private AddressType addressType;

    @Schema(description = "Recipient name", example = "Jane Doe")
    private String recipientName;

    @Schema(description = "Primary phone number", example = "+8801700000000")
    private String phone;

    @Schema(description = "Alternate phone number", example = "+8801800000000")
    private String alternatePhone;

    @Schema(description = "House or flat details", example = "Flat 4B, House 12, Road 5")
    private String houseNo;

    @Schema(description = "Street address line", example = "Dhanmondi R/A")
    private String street;

    @Schema(description = "Nearby landmark", example = "Near Abahani Playground")
    private String landmark;

    @Schema(description = "Area or neighborhood", example = "Dhanmondi")
    private String area;

    @Schema(description = "City or Upazila name", example = "Dhaka")
    private String city;

    @Schema(description = "District name", example = "Dhaka")
    private String district;

    @Schema(description = "Division name", example = "Dhaka Division")
    private String division;

    @Schema(description = "State name", example = "Dhaka")
    private String state;

    @Schema(description = "Country name", example = "Bangladesh")
    private String country;

    @Schema(description = "Postal code", example = "1205")
    private String postalCode;

    @Schema(description = "GPS Latitude", example = "23.7461")
    private Double latitude;

    @Schema(description = "GPS Longitude", example = "90.3742")
    private Double longitude;

    @Schema(description = "Is default overall address", example = "true")
    private boolean isDefault;

    @Schema(description = "Is default shipping address", example = "true")
    private boolean defaultShipping;

    @Schema(description = "Is default billing address", example = "false")
    private boolean defaultBilling;

    @Schema(description = "Creation timestamp", example = "2026-08-05T14:00:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp", example = "2026-08-05T14:00:00Z")
    private Instant updatedAt;
}
