package com.example.ecommerce.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Warehouse projection response DTO.
 */
@Schema(description = "Warehouse response")
public class WarehouseResponse {

    @Schema(description = "Warehouse ID", example = "1")
    private Long id;

    @Schema(description = "Warehouse code", example = "WH-CENTRAL-01")
    private String code;

    @Schema(description = "Warehouse name", example = "Central Distribution Hub")
    private String name;

    @Schema(description = "Location address", example = "100 Logistics Way")
    private String location;

    @Schema(description = "Contact person", example = "John Doe")
    private String contactPerson;

    @Schema(description = "Phone", example = "+1-555-0192")
    private String phone;

    @Schema(description = "Email", example = "warehouse@example.com")
    private String email;

    @Schema(description = "Capacity limit", example = "50000")
    private Integer capacity;

    @Schema(description = "Active indicator", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    @Schema(description = "Last update timestamp")
    private Instant updatedAt;

    public WarehouseResponse() {
    }

    public WarehouseResponse(Long id, String code, String name, String location, String contactPerson, String phone, String email, Integer capacity, boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.location = location;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.capacity = capacity;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static WarehouseResponseBuilder builder() { return new WarehouseResponseBuilder(); }

    public static class WarehouseResponseBuilder {
        private Long id;
        private String code;
        private String name;
        private String location;
        private String contactPerson;
        private String phone;
        private String email;
        private Integer capacity;
        private boolean active;
        private Instant createdAt;
        private Instant updatedAt;

        WarehouseResponseBuilder() {}

        public WarehouseResponseBuilder id(Long id) { this.id = id; return this; }
        public WarehouseResponseBuilder code(String code) { this.code = code; return this; }
        public WarehouseResponseBuilder name(String name) { this.name = name; return this; }
        public WarehouseResponseBuilder location(String location) { this.location = location; return this; }
        public WarehouseResponseBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public WarehouseResponseBuilder phone(String phone) { this.phone = phone; return this; }
        public WarehouseResponseBuilder email(String email) { this.email = email; return this; }
        public WarehouseResponseBuilder capacity(Integer capacity) { this.capacity = capacity; return this; }
        public WarehouseResponseBuilder active(boolean active) { this.active = active; return this; }
        public WarehouseResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public WarehouseResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public WarehouseResponse build() {
            return new WarehouseResponse(id, code, name, location, contactPerson, phone, email, capacity, active, createdAt, updatedAt);
        }
    }
}
