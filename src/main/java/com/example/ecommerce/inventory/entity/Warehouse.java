package com.example.ecommerce.inventory.entity;

import com.example.ecommerce.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

/**
 * Physical or logical warehouse storage facility entity.
 */
@Entity
@Table(
        name = "warehouses",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_warehouses_code", columnNames = "code"),
                @UniqueConstraint(name = "uk_warehouses_name", columnNames = "name")
        }
)
@SQLDelete(sql = "UPDATE warehouses SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Warehouse extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 250)
    private String location;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(length = 30)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private Integer capacity = 0;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Warehouse() {
    }

    public Warehouse(String code, String name, String location, String contactPerson, String phone, String email, Integer capacity, boolean active, boolean deleted, Instant deletedAt) {
        this.code = code;
        this.name = name;
        this.location = location;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.capacity = capacity != null ? capacity : 0;
        this.active = active;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static WarehouseBuilder builder() { return new WarehouseBuilder(); }

    public static class WarehouseBuilder {
        private String code;
        private String name;
        private String location;
        private String contactPerson;
        private String phone;
        private String email;
        private Integer capacity = 0;
        private boolean active = true;
        private boolean deleted = false;
        private Instant deletedAt;

        WarehouseBuilder() {}

        public WarehouseBuilder code(String code) { this.code = code; return this; }
        public WarehouseBuilder name(String name) { this.name = name; return this; }
        public WarehouseBuilder location(String location) { this.location = location; return this; }
        public WarehouseBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public WarehouseBuilder phone(String phone) { this.phone = phone; return this; }
        public WarehouseBuilder email(String email) { this.email = email; return this; }
        public WarehouseBuilder capacity(Integer capacity) { this.capacity = capacity; return this; }
        public WarehouseBuilder active(boolean active) { this.active = active; return this; }
        public WarehouseBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public WarehouseBuilder deletedAt(Instant deletedAt) { this.deletedAt = deletedAt; return this; }

        public Warehouse build() {
            return new Warehouse(code, name, location, contactPerson, phone, email, capacity, active, deleted, deletedAt);
        }
    }
}
