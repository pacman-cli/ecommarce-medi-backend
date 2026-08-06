package com.example.ecommerce.user.entity;

import com.example.ecommerce.address.dto.enums.AddressType;
import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * Customer shipping or billing address entity supporting administrative geography
 * divisions, GPS coordinates, default flags, and soft delete.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "addresses",
        indexes = {
                @Index(name = "idx_addresses_user", columnList = "user_id"),
                @Index(name = "idx_addresses_type", columnList = "address_type")
        }
)
@SQLDelete(sql = "UPDATE addresses SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Address extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Short descriptor such as HOME, WORK, PHARMACY. */
    @Column(nullable = false, length = 30)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false, length = 20)
    @Builder.Default
    private AddressType addressType = AddressType.BOTH;

    @Column(name = "recipient_name", nullable = false, length = 120)
    private String recipientName;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(name = "alternate_phone", length = 30)
    private String alternatePhone;

    @Column(name = "house_no", length = 60)
    private String houseNo;

    @Column(nullable = false, length = 120)
    private String street;

    @Column(length = 120)
    private String landmark;

    @Column(length = 80)
    private String area;

    @Column(nullable = false, length = 60)
    private String city;

    @Column(length = 60)
    private String district;

    @Column(length = 60)
    private String division;

    @Column(length = 60)
    private String state;

    @Column(nullable = false, length = 60)
    @Builder.Default
    private String country = "Bangladesh";

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private boolean isDefault = false;

    @Column(name = "default_shipping", nullable = false)
    @Builder.Default
    private boolean defaultShipping = false;

    @Column(name = "default_billing", nullable = false)
    @Builder.Default
    private boolean defaultBilling = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
