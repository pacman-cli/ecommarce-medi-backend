package com.example.ecommerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for creating or updating a user address.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @NotBlank(message = "Label is required")
    @Size(max = 30, message = "Label must not exceed 30 characters")
    private String label;

    @Size(max = 120, message = "Recipient name must not exceed 120 characters")
    private String recipientName;

    @Size(max = 30, message = "Phone must not exceed 30 characters")
    @Pattern(regexp = "^[+0-9\\s-]*$", message = "Phone may only contain digits, spaces, plus and dashes")
    private String phone;

    @NotBlank(message = "Street address is required")
    @Size(max = 120, message = "Street address must not exceed 120 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 60, message = "City must not exceed 60 characters")
    private String city;

    @Size(max = 60, message = "State must not exceed 60 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 60, message = "Country must not exceed 60 characters")
    private String country;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    /** When {@code true}, this address becomes the user's default. */
    private boolean isDefault;
}
