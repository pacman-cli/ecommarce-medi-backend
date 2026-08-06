package com.example.ecommerce.delivery.entity;

import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
import com.example.ecommerce.delivery.dto.enums.ShippingMethod;
import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise shipment aggregate root tracking order logistics, rider assignment,
 * COD collection, tracking number, and delivery status.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "delivery_shipments",
        indexes = {
                @Index(name = "idx_shipment_tracking_number", columnList = "tracking_number"),
                @Index(name = "idx_shipment_order_id", columnList = "order_id"),
                @Index(name = "idx_shipment_status", columnList = "status")
        }
)
@SQLDelete(sql = "UPDATE delivery_shipments SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class DeliveryShipment extends BaseEntity {

    @Column(name = "shipment_number", nullable = false, length = 50, unique = true)
    private String shipmentNumber;

    @Column(name = "tracking_number", nullable = false, length = 100, unique = true)
    private String trackingNumber;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private DeliveryPartner partner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private DeliveryZone zone;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method", nullable = false, length = 30)
    @Builder.Default
    private ShippingMethod shippingMethod = ShippingMethod.STANDARD;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private DeliveryStatus status = DeliveryStatus.UNASSIGNED;

    @Column(name = "recipient_name", nullable = false, length = 120)
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false, length = 30)
    private String recipientPhone;

    @Column(name = "shipping_address", nullable = false, length = 500)
    private String shippingAddress;

    @Column(name = "rider_name", length = 100)
    private String riderName;

    @Column(name = "rider_phone", length = 30)
    private String riderPhone;

    @Column(name = "vehicle_info", length = 100)
    private String vehicleInfo;

    @Column(name = "is_cod", nullable = false)
    @Builder.Default
    private boolean isCod = false;

    @Column(name = "cod_amount", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal codAmount = BigDecimal.ZERO;

    @Column(name = "cod_fee", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal codFee = BigDecimal.ZERO;

    @Column(name = "delivery_fee", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "scheduled_time_slot", length = 50)
    private String scheduledTimeSlot;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Column(length = 500)
    private String notes;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timestamp ASC")
    @Builder.Default
    private List<DeliveryTimeline> timelines = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public void addTimeline(DeliveryStatus status, String location, String note, String updatedBy) {
        if (this.timelines == null) {
            this.timelines = new ArrayList<>();
        }
        DeliveryTimeline timeline = DeliveryTimeline.builder()
                .shipment(this)
                .status(status)
                .location(location)
                .note(note)
                .updatedBy(updatedBy)
                .timestamp(Instant.now())
                .build();
        this.timelines.add(timeline);
    }
}
