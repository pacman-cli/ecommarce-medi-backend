package com.example.ecommerce.auth.dto.request;

import com.example.ecommerce.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for setting a new password using a previously issued reset code.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Reset code is required")
    @Pattern(regexp = "^\\d{6}$", message = "Reset code must be a 6-digit number")
    private String code;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @ValidPassword
    private String newPassword;
}
