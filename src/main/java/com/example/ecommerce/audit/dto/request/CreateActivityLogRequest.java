package com.example.ecommerce.audit.dto.request;

import com.example.ecommerce.audit.dto.enums.ActivityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for programmatically recording a user/admin activity event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Activity event recording request payload")
public class CreateActivityLogRequest {

    @Schema(description = "Activity type tag", example = "ADMIN_ACTION")
    @NotNull(message = "Activity type is required")
    private ActivityType activityType;

    @Schema(description = "Module tag", example = "INVENTORY")
    private String module;

    @Schema(description = "Human readable action description", example = "Adjusted stock quantity for Product #200")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "JSON metadata string payload", example = "{\"adjustedQuantity\":-5, \"reason\":\"Damaged box\"}")
    private String metadata;

    @Schema(description = "Is administrative security activity flag", example = "true")
    @Builder.Default
    private Boolean isAdminActivity = false;
}
