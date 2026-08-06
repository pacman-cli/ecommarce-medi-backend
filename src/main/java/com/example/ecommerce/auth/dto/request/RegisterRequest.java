package com.example.ecommerce.auth.dto.request;

import com.example.ecommerce.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for creating a new customer account.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @ValidPassword
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 60, message = "First name must not exceed 60 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 60, message = "Last name must not exceed 60 characters")
    private String lastName;
}
