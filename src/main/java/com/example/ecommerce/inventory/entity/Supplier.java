package com.example.ecommerce.inventory.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.supplier.dto.enums.SupplierStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

/**
 * Enterprise vendor and goods supplier entity supporting trade licenses, TIN,
 * status lifecycle, contact details, and soft delete.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "suppliers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_suppliers_code", columnNames = "code"),
                @UniqueConstraint(name = "uk_suppliers_name", columnNames = "name")
        },
        indexes = {
                @Index(name = "idx_suppliers_code", columnList = "code"),
                @Index(name = "idx_suppliers_status", columnList = "status")
        }
)
@SQLDelete(sql = "UPDATE suppliers SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Supplier extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(length = 100)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(length = 250)
    private String address;

    @Column(name = "tax_number", length = 50)
    private String taxNumber;

    @Column(name = "trade_license", length = 100)
    private String tradeLicense;

    @Column(name = "tin", length = 50)
    private String tin;

    @Column(length = 200)
    private String website;

    @Column(length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private SupplierStatus status = SupplierStatus.ACTIVE;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public String getCompanyName() {
        return name;
    }

    public void setCompanyName(String companyName) {
        this.name = companyName;
    }
}
