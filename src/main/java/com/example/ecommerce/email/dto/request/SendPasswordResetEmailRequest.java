package com.example.ecommerce.email.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Send password reset email request payload")
public class SendPasswordResetEmailRequest {

    @Schema(description = "Recipient email address", example = "john.doe@example.com")
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipientEmail;

    @Schema(description = "User name", example = "John Doe")
    private String userName;

    @Schema(description = "OTP verification code", example = "889901")
    @NotBlank(message = "OTP code is required")
    private String resetOtp;
}
