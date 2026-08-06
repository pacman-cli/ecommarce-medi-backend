package com.example.ecommerce.order.dto.response;

import com.example.ecommerce.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Historical status transition response DTO.
 */
@Schema(description = "Order timeline status event details")
public class OrderTimelineResponse {

    @Schema(description = "Timeline entry ID", example = "1")
    private Long id;

    @Schema(description = "Order status", example = "SHIPPED")
    private OrderStatus status;

    @Schema(description = "Audit note", example = "Handed over to FedEx carrier")
    private String note;

    @Schema(description = "Timestamp of transition")
    private Instant changedAt;

    @Schema(description = "User or system agent making change", example = "admin@example.com")
    private String changedBy;

    public OrderTimelineResponse() {
    }

    public OrderTimelineResponse(Long id, OrderStatus status, String note, Instant changedAt, String changedBy) {
        this.id = id;
        this.status = status;
        this.note = note;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Instant getChangedAt() { return changedAt; }
    public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public static OrderTimelineResponseBuilder builder() { return new OrderTimelineResponseBuilder(); }

    public static class OrderTimelineResponseBuilder {
        private Long id;
        private OrderStatus status;
        private String note;
        private Instant changedAt;
        private String changedBy;

        OrderTimelineResponseBuilder() {}

        public OrderTimelineResponseBuilder id(Long id) { this.id = id; return this; }
        public OrderTimelineResponseBuilder status(OrderStatus status) { this.status = status; return this; }
        public OrderTimelineResponseBuilder note(String note) { this.note = note; return this; }
        public OrderTimelineResponseBuilder changedAt(Instant changedAt) { this.changedAt = changedAt; return this; }
        public OrderTimelineResponseBuilder changedBy(String changedBy) { this.changedBy = changedBy; return this; }

        public OrderTimelineResponse build() {
            return new OrderTimelineResponse(id, status, note, changedAt, changedBy);
        }
    }
}
