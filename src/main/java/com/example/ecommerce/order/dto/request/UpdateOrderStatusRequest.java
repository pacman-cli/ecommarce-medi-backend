package com.example.ecommerce.order.dto.request;

import com.example.ecommerce.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload for updating an order's lifecycle status and tracking code.
 */
@Schema(description = "Payload for updating order status")
public class UpdateOrderStatusRequest {

    @NotNull(message = "Order status is required")
    @Schema(description = "Target status transition", example = "SHIPPED")
    private OrderStatus status;

    @Size(max = 100, message = "Tracking number must not exceed 100 characters")
    @Schema(description = "Carrier tracking number (required for SHIPPED status)", example = "TRACK-FDX-998811")
    private String trackingNumber;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    @Schema(description = "Status change audit note", example = "Handed over to carrier FedEx")
    private String note;

    public UpdateOrderStatusRequest() {
    }

    public UpdateOrderStatusRequest(OrderStatus status, String trackingNumber, String note) {
        this.status = status;
        this.trackingNumber = trackingNumber;
        this.note = note;
    }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public static UpdateOrderStatusRequestBuilder builder() { return new UpdateOrderStatusRequestBuilder(); }

    public static class UpdateOrderStatusRequestBuilder {
        private OrderStatus status;
        private String trackingNumber;
        private String note;

        UpdateOrderStatusRequestBuilder() {}

        public UpdateOrderStatusRequestBuilder status(OrderStatus status) { this.status = status; return this; }
        public UpdateOrderStatusRequestBuilder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public UpdateOrderStatusRequestBuilder note(String note) { this.note = note; return this; }

        public UpdateOrderStatusRequest build() {
            return new UpdateOrderStatusRequest(status, trackingNumber, note);
        }
    }
}
