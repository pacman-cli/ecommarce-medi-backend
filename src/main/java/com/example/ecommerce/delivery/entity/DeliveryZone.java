package com.example.ecommerce.delivery.entity;

import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
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

/**
 * Geographic delivery zone entity specifying base rates, surcharges, and delivery day ranges.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "delivery_zones",
        indexes = {
                @Index(name = "idx_delivery_zone_code", columnList = "code")
        }
)
@SQLDelete(sql = "UPDATE delivery_zones SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class DeliveryZone extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String code;

    @Column(length = 60)
    private String division;

    @Column(length = 60)
    private String district;

    @Column(name = "base_fee", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal baseFee = new BigDecimal("60.00");

    @Column(name = "express_fee", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal expressFee = new BigDecimal("120.00");

    @Column(name = "cod_fee", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal codFee = new BigDecimal("15.00");

    @Column(name = "min_delivery_days", nullable = false)
    @Builder.Default
    private Integer minDeliveryDays = 1;

    @Column(name = "max_delivery_days", nullable = false)
    @Builder.Default
    private Integer maxDeliveryDays = 3;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
