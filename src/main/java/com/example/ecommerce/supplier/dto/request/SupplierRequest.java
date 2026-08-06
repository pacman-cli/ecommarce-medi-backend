package com.example.ecommerce.supplier.dto.request;

import com.example.ecommerce.supplier.dto.enums.SupplierStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for creating or updating a vendor supplier profile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Supplier creation and update request payload")
public class SupplierRequest {

    @Schema(description = "Unique supplier code identifier", example = "SUP-PHARMA-01")
    @NotBlank(message = "Supplier code is required")
    @Size(max = 50, message = "Code cannot exceed 50 characters")
    private String code;

    @Schema(description = "Company or vendor name", example = "Square Pharmaceuticals PLC")
    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    private String name;

    @Schema(description = "Primary contact person name", example = "Dr. Rafiqul Islam")
    @Size(max = 100, message = "Contact person cannot exceed 100 characters")
    private String contactPerson;

    @Schema(description = "Contact email address", example = "info@squarepharma.com.bd")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Schema(description = "Contact phone number", example = "+88028833047")
    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    private String phone;

    @Schema(description = "Office/Warehouse physical address", example = "Square Centre, 48 Mohakhali C/A, Dhaka 1212")
    @Size(max = 250, message = "Address cannot exceed 250 characters")
    private String address;

    @Schema(description = "Tax Identification / VAT Registration Number", example = "TIN-987654321")
    @Size(max = 50, message = "Tax number cannot exceed 50 characters")
    private String taxNumber;

    @Schema(description = "Trade License Number", example = "TL-DHAKA-2026-998877")
    @Size(max = 100, message = "Trade license cannot exceed 100 characters")
    private String tradeLicense;

    @Schema(description = "TIN (Taxpayer Identification Number)", example = "TIN-1234567890")
    @Size(max = 50, message = "TIN cannot exceed 50 characters")
    private String tin;

    @Schema(description = "Official website URL", example = "https://www.squarepharma.com.bd")
    @Size(max = 200, message = "Website cannot exceed 200 characters")
    private String website;

    @Schema(description = "Additional notes or description", example = "Key supplier for paracetamol and antibiotic formulations")
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @Schema(description = "Supplier status lifecycle", example = "ACTIVE")
    @NotNull(message = "Supplier status is required")
    @Builder.Default
    private SupplierStatus status = SupplierStatus.ACTIVE;

    @Schema(description = "Is active status flag", example = "true")
    @Builder.Default
    private Boolean active = true;
}
