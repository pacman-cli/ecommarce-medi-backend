package com.example.ecommerce.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Supplier response DTO.
 */
@Schema(description = "Supplier details response")
public class SupplierResponse {

    @Schema(description = "Supplier ID", example = "10")
    private Long id;

    @Schema(description = "Supplier code", example = "SUP-PHARMA-01")
    private String code;

    @Schema(description = "Supplier name", example = "Global PharmaCare Labs")
    private String name;

    @Schema(description = "Contact person", example = "Jane Smith")
    private String contactPerson;

    @Schema(description = "Email address", example = "sales@pharmacare.com")
    private String email;

    @Schema(description = "Phone number", example = "+1-800-555-0199")
    private String phone;

    @Schema(description = "Physical address", example = "500 Pharma Blvd")
    private String address;

    @Schema(description = "Tax number", example = "TAX-99887766")
    private String taxNumber;

    @Schema(description = "Active status flag", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    @Schema(description = "Last update timestamp")
    private Instant updatedAt;

    public SupplierResponse() {
    }

    public SupplierResponse(Long id, String code, String name, String contactPerson, String email, String phone, String address, String taxNumber, boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.taxNumber = taxNumber;
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

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTaxNumber() { return taxNumber; }
    public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static SupplierResponseBuilder builder() { return new SupplierResponseBuilder(); }

    public static class SupplierResponseBuilder {
        private Long id;
        private String code;
        private String name;
        private String contactPerson;
        private String email;
        private String phone;
        private String address;
        private String taxNumber;
        private boolean active;
        private Instant createdAt;
        private Instant updatedAt;

        SupplierResponseBuilder() {}

        public SupplierResponseBuilder id(Long id) { this.id = id; return this; }
        public SupplierResponseBuilder code(String code) { this.code = code; return this; }
        public SupplierResponseBuilder name(String name) { this.name = name; return this; }
        public SupplierResponseBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public SupplierResponseBuilder email(String email) { this.email = email; return this; }
        public SupplierResponseBuilder phone(String phone) { this.phone = phone; return this; }
        public SupplierResponseBuilder address(String address) { this.address = address; return this; }
        public SupplierResponseBuilder taxNumber(String taxNumber) { this.taxNumber = taxNumber; return this; }
        public SupplierResponseBuilder active(boolean active) { this.active = active; return this; }
        public SupplierResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public SupplierResponseBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public SupplierResponse build() {
            return new SupplierResponse(id, code, name, contactPerson, email, phone, address, taxNumber, active, createdAt, updatedAt);
        }
    }
}
