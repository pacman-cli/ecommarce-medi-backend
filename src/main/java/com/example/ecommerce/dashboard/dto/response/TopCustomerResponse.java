package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Top customer DTO ranked by lifetime or period total spend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Highest spending customer item")
public class TopCustomerResponse {

    @Schema(description = "Customer User ID", example = "204")
    private Long userId;

    @Schema(description = "Customer Full Name", example = "Robert Smith")
    private String fullName;

    @Schema(description = "Customer Email", example = "robert.smith@example.com")
    private String email;

    @Schema(description = "Customer Phone", example = "+1234567890")
    private String phone;

    @Schema(description = "Total completed orders count", example = "15")
    private Long totalOrders;

    @Schema(description = "Total cumulative expenditure", example = "3450.75")
    private BigDecimal totalSpent;

    @Schema(description = "Date of last completed order", example = "2026-08-02T10:15:00Z")
    private Instant lastOrderDate;
}
