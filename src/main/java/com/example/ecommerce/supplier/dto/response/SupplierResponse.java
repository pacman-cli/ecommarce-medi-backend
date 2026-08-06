package com.example.ecommerce.supplier.dto.response;

import com.example.ecommerce.supplier.dto.enums.SupplierStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Supplier response DTO payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Supplier profile summary payload")
public class SupplierResponse {

    @Schema(description = "Supplier ID", example = "10")
    private Long id;

    @Schema(description = "Supplier code", example = "SUP-PHARMA-01")
    private String code;

    @Schema(description = "Company or vendor name", example = "Square Pharmaceuticals PLC")
    private String name;

    @Schema(description = "Company or vendor name alias", example = "Square Pharmaceuticals PLC")
    private String companyName;

    @Schema(description = "Contact person", example = "Dr. Rafiqul Islam")
    private String contactPerson;

    @Schema(description = "Email", example = "info@squarepharma.com.bd")
    private String email;

    @Schema(description = "Phone", example = "+88028833047")
    private String phone;

    @Schema(description = "Address", example = "Square Centre, 48 Mohakhali C/A, Dhaka 1212")
    private String address;

    @Schema(description = "Tax identification number", example = "TIN-987654321")
    private String taxNumber;

    @Schema(description = "Trade license number", example = "TL-DHAKA-2026-998877")
    private String tradeLicense;

    @Schema(description = "TIN number", example = "TIN-1234567890")
    private String tin;

    @Schema(description = "Website", example = "https://www.squarepharma.com.bd")
    private String website;

    @Schema(description = "Notes", example = "Key supplier for paracetamol and antibiotic formulations")
    private String notes;

    @Schema(description = "Status lifecycle", example = "ACTIVE")
    private SupplierStatus status;

    @Schema(description = "Active status flag", example = "true")
    private boolean active;

    @Schema(description = "Creation timestamp", example = "2026-08-05T14:00:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp", example = "2026-08-05T14:00:00Z")
    private Instant updatedAt;
}
