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
@Schema(description = "Send welcome email request payload")
public class SendWelcomeEmailRequest {

    @Schema(description = "Recipient email address", example = "john.doe@example.com")
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipientEmail;

    @Schema(description = "User full name", example = "John Doe")
    @NotBlank(message = "User name is required")
    private String userName;

    @Schema(description = "Action landing URL", example = "https://store.example.com")
    private String actionUrl;
}
