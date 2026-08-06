package com.example.ecommerce.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for creating or updating a supplier vendor.
 */
@Schema(description = "Payload for supplier management")
public class SupplierRequest {

    @NotBlank(message = "Supplier code is required")
    @Size(max = 50, message = "Supplier code must not exceed 50 characters")
    @Schema(description = "Unique supplier code", example = "SUP-PHARMA-01")
    private String code;

    @NotBlank(message = "Supplier name is required")
    @Size(max = 100, message = "Supplier name must not exceed 100 characters")
    @Schema(description = "Supplier company name", example = "Global PharmaCare Labs")
    private String name;

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    @Schema(description = "Primary contact person", example = "Jane Smith")
    private String contactPerson;

    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Supplier email address", example = "sales@pharmacare.com")
    private String email;

    @Size(max = 30, message = "Phone must not exceed 30 characters")
    @Schema(description = "Supplier phone number", example = "+1-800-555-0199")
    private String phone;

    @Size(max = 250, message = "Address must not exceed 250 characters")
    @Schema(description = "Physical address", example = "500 Pharma Blvd, Suite 400")
    private String address;

    @Size(max = 50, message = "Tax number must not exceed 50 characters")
    @Schema(description = "Tax identification number", example = "TAX-99887766")
    private String taxNumber;

    @Schema(description = "Active operational status flag", example = "true")
    private Boolean active;

    public SupplierRequest() {
    }

    public SupplierRequest(String code, String name, String contactPerson, String email, String phone, String address, String taxNumber, Boolean active) {
        this.code = code;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.taxNumber = taxNumber;
        this.active = active;
    }

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

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public static SupplierRequestBuilder builder() { return new SupplierRequestBuilder(); }

    public static class SupplierRequestBuilder {
        private String code;
        private String name;
        private String contactPerson;
        private String email;
        private String phone;
        private String address;
        private String taxNumber;
        private Boolean active;

        SupplierRequestBuilder() {}

        public SupplierRequestBuilder code(String code) { this.code = code; return this; }
        public SupplierRequestBuilder name(String name) { this.name = name; return this; }
        public SupplierRequestBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public SupplierRequestBuilder email(String email) { this.email = email; return this; }
        public SupplierRequestBuilder phone(String phone) { this.phone = phone; return this; }
        public SupplierRequestBuilder address(String address) { this.address = address; return this; }
        public SupplierRequestBuilder taxNumber(String taxNumber) { this.taxNumber = taxNumber; return this; }
        public SupplierRequestBuilder active(Boolean active) { this.active = active; return this; }

        public SupplierRequest build() {
            return new SupplierRequest(code, name, contactPerson, email, phone, address, taxNumber, active);
        }
    }
}
