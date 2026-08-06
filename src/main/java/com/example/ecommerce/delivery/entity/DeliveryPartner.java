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

import java.time.Instant;

/**
 * Entity representing a logistics carrier delivery partner (e.g. Steadfast, RedX, Pathao, Internal Fleet).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "delivery_partners",
        indexes = {
                @Index(name = "idx_delivery_partner_code", columnList = "code")
        }
)
@SQLDelete(sql = "UPDATE delivery_partners SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class DeliveryPartner extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String code;

    @Column(name = "contact_phone", length = 30)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "api_endpoint", length = 250)
    private String apiEndpoint;

    @Column(name = "api_key", length = 250)
    private String apiKey;

    @Column(name = "cod_supported", nullable = false)
    @Builder.Default
    private boolean codSupported = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
