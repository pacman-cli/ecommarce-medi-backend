package com.example.ecommerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for a user updating their own profile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 60, message = "First name must not exceed 60 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 60, message = "Last name must not exceed 60 characters")
    private String lastName;

    @Size(max = 30, message = "Phone must not exceed 30 characters")
    @Pattern(regexp = "^[+0-9\\s-]*$", message = "Phone may only contain digits, spaces, plus and dashes")
    private String phone;
}
