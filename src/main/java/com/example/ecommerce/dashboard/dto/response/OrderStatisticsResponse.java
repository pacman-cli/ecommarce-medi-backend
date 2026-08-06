package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Breakdown analytics for order statuses, payment statuses, and fulfillment rates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order state statistics and status breakdown payload")
public class OrderStatisticsResponse {

    @Schema(description = "Total overall orders count", example = "450")
    private Long totalOrders;

    @Schema(description = "Pending fulfillment count", example = "25")
    private Long pendingOrders;

    @Schema(description = "Confirmed orders count", example = "40")
    private Long confirmedOrders;

    @Schema(description = "Currently packed count", example = "35")
    private Long packedOrders;

    @Schema(description = "Shipped orders in transit count", example = "50")
    private Long shippedOrders;

    @Schema(description = "Successfully delivered count", example = "280")
    private Long deliveredOrders;

    @Schema(description = "Cancelled orders count", example = "15")
    private Long cancelledOrders;

    @Schema(description = "Returned orders count", example = "8")
    private Long returnedOrders;

    @Schema(description = "Refunded orders count", example = "5")
    private Long refundedOrders;

    @Schema(description = "Order status pie chart distribution segments")
    private List<PieChartSegmentDto> statusBreakdown;

    @Schema(description = "Payment status distribution segments")
    private List<PieChartSegmentDto> paymentStatusBreakdown;
}
