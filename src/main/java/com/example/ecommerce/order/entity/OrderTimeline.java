package com.example.ecommerce.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Historical audit log of order status transitions and administrative notes.
 */
@Entity
@Table(name = "order_timelines")
public class OrderTimeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(length = 500)
    private String note;

    @Column(name = "changed_at", nullable = false)
    private Instant changedAt = Instant.now();

    @Column(name = "changed_by", length = 100)
    private String changedBy;

    public OrderTimeline() {
    }

    public OrderTimeline(Long id, Order order, OrderStatus status, String note, Instant changedAt, String changedBy) {
        this.id = id;
        this.order = order;
        this.status = status;
        this.note = note;
        this.changedAt = changedAt != null ? changedAt : Instant.now();
        this.changedBy = changedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Instant getChangedAt() { return changedAt; }
    public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public static OrderTimelineBuilder builder() { return new OrderTimelineBuilder(); }

    public static class OrderTimelineBuilder {
        private Long id;
        private Order order;
        private OrderStatus status;
        private String note;
        private Instant changedAt = Instant.now();
        private String changedBy;

        OrderTimelineBuilder() {}

        public OrderTimelineBuilder id(Long id) { this.id = id; return this; }
        public OrderTimelineBuilder order(Order order) { this.order = order; return this; }
        public OrderTimelineBuilder status(OrderStatus status) { this.status = status; return this; }
        public OrderTimelineBuilder note(String note) { this.note = note; return this; }
        public OrderTimelineBuilder changedAt(Instant changedAt) { this.changedAt = changedAt; return this; }
        public OrderTimelineBuilder changedBy(String changedBy) { this.changedBy = changedBy; return this; }

        public OrderTimeline build() {
            return new OrderTimeline(id, order, status, note, changedAt, changedBy);
        }
    }
}
