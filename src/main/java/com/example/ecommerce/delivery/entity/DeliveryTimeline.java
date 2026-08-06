package com.example.ecommerce.delivery.entity;

import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Audit log timeline entry tracking delivery shipment status updates.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_timelines")
public class DeliveryTimeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
    private DeliveryShipment shipment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeliveryStatus status;

    @Column(length = 150)
    private String location;

    @Column(length = 300)
    private String note;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(nullable = false)
    @Builder.Default
    private Instant timestamp = Instant.now();
}
