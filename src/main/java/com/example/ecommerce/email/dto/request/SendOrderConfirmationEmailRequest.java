package com.example.ecommerce.email.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Send order confirmation email request payload")
public class SendOrderConfirmationEmailRequest {

    @Schema(description = "Recipient email address", example = "john.doe@example.com")
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipientEmail;

    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;

    @Schema(description = "Order number", example = "ORD-20260805-9988")
    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @Schema(description = "Total order amount", example = "150.00")
    private BigDecimal totalAmount;

    @Schema(description = "Shipping address text", example = "123 Main Street, Dhaka")
    private String shippingAddress;

    @Schema(description = "List of ordered items")
    private List<OrderItemEntry> orderItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemEntry {
        private String productName;
        private Integer quantity;
        private String price;
    }
}
