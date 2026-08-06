package com.example.ecommerce.supplier.dto.request;

import com.example.ecommerce.supplier.dto.enums.SupplierStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request criteria payload for filtering suppliers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Supplier search criteria filtering payload")
public class SupplierFilterRequest {

    @Schema(description = "Search query keyword matching company name, code, contact person, or email", example = "Square")
    private String query;

    @Schema(description = "Supplier status filter", example = "ACTIVE")
    private SupplierStatus status;

    @Schema(description = "Trade license number filter", example = "TL-DHAKA")
    private String tradeLicense;

    @Schema(description = "TIN number filter", example = "TIN-12345")
    private String tin;

    @Schema(description = "Active status filter", example = "true")
    private Boolean active;
}
