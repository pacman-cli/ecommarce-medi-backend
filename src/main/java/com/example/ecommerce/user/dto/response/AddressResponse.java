package com.example.ecommerce.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Address projection returned by the API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private Long id;
    private String label;
    private String recipientName;
    private String phone;
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private boolean isDefault;
    private Instant createdAt;
    private Instant updatedAt;
}
