package com.example.ecommerce.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for creating or updating a warehouse facility.
 */
@Schema(description = "Payload for warehouse management")
public class WarehouseRequest {

    @NotBlank(message = "Warehouse code is required")
    @Size(max = 50, message = "Warehouse code must not exceed 50 characters")
    @Schema(description = "Unique warehouse code", example = "WH-CENTRAL-01")
    private String code;

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 100, message = "Warehouse name must not exceed 100 characters")
    @Schema(description = "Warehouse display name", example = "Central Distribution Hub")
    private String name;

    @Size(max = 250, message = "Location must not exceed 250 characters")
    @Schema(description = "Physical address or location", example = "100 Logistics Way, Industrial Zone")
    private String location;

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    @Schema(description = "Facility manager name", example = "John Doe")
    private String contactPerson;

    @Size(max = 30, message = "Phone must not exceed 30 characters")
    @Schema(description = "Contact phone number", example = "+1-555-0192")
    private String phone;

    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Contact email address", example = "warehouse@example.com")
    private String email;

    @Min(value = 0, message = "Capacity must be non-negative")
    @Schema(description = "Storage capacity unit limit", example = "50000")
    private Integer capacity;

    @Schema(description = "Active operational status flag", example = "true")
    private Boolean active;

    public WarehouseRequest() {
    }

    public WarehouseRequest(String code, String name, String location, String contactPerson, String phone, String email, Integer capacity, Boolean active) {
        this.code = code;
        this.name = name;
        this.location = location;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.capacity = capacity;
        this.active = active;
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

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public static WarehouseRequestBuilder builder() { return new WarehouseRequestBuilder(); }

    public static class WarehouseRequestBuilder {
        private String code;
        private String name;
        private String location;
        private String contactPerson;
        private String phone;
        private String email;
        private Integer capacity;
        private Boolean active;

        WarehouseRequestBuilder() {}

        public WarehouseRequestBuilder code(String code) { this.code = code; return this; }
        public WarehouseRequestBuilder name(String name) { this.name = name; return this; }
        public WarehouseRequestBuilder location(String location) { this.location = location; return this; }
        public WarehouseRequestBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public WarehouseRequestBuilder phone(String phone) { this.phone = phone; return this; }
        public WarehouseRequestBuilder email(String email) { this.email = email; return this; }
        public WarehouseRequestBuilder capacity(Integer capacity) { this.capacity = capacity; return this; }
        public WarehouseRequestBuilder active(Boolean active) { this.active = active; return this; }

        public WarehouseRequest build() {
            return new WarehouseRequest(code, name, location, contactPerson, phone, email, capacity, active);
        }
    }
}
