package com.example.ecommerce.dashboard.dto.response;

import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Summary DTO of recent order placements for quick dashboard inspection.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Recent order item view for dashboard listing")
public class RecentOrderResponse {

    @Schema(description = "Order ID", example = "501")
    private Long id;

    @Schema(description = "Unique human-readable order tracking code", example = "ORD-20260804-98421")
    private String orderNumber;

    @Schema(description = "Customer full name", example = "Jane Doe")
    private String customerName;

    @Schema(description = "Customer email address", example = "jane.doe@example.com")
    private String customerEmail;

    @Schema(description = "Total items count in order", example = "3")
    private Integer itemCount;

    @Schema(description = "Grand total order monetary value", example = "185.50")
    private BigDecimal grandTotal;

    @Schema(description = "Fulfillment state", example = "PROCESSING")
    private OrderStatus orderStatus;

    @Schema(description = "Payment processing state", example = "COMPLETED")
    private PaymentStatus paymentStatus;

    @Schema(description = "Order creation timestamp", example = "2026-08-04T14:30:00Z")
    private Instant createdAt;
}
