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
@Schema(description = "Send order status email request payload")
public class SendOrderStatusEmailRequest {

    @Schema(description = "Recipient email address", example = "john.doe@example.com")
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipientEmail;

    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;

    @Schema(description = "Order number", example = "ORD-20260805-9988")
    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @Schema(description = "New order status", example = "SHIPPED")
    @NotBlank(message = "Order status is required")
    private String orderStatus;

    @Schema(description = "Status description message", example = "Your package has been handed over to the courier partner.")
    private String statusMessage;

    @Schema(description = "Tracking number", example = "TRK-2026-9901")
    private String trackingNumber;
}
