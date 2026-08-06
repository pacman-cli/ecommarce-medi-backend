package com.example.ecommerce.delivery.dto.response;

import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO representing an individual delivery status update timeline checkpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delivery status timeline update checkpoint")
public class DeliveryTimelineResponse {

    @Schema(description = "Timeline entry ID", example = "1")
    private Long id;

    @Schema(description = "Delivery status at checkpoint", example = "IN_TRANSIT")
    private DeliveryStatus status;

    @Schema(description = "Current location", example = "Dhaka Central Sorting Hub")
    private String location;

    @Schema(description = "Update note", example = "Dispatched on vehicle #4")
    private String note;

    @Schema(description = "Updated by user or system identifier", example = "admin@example.com")
    private String updatedBy;

    @Schema(description = "Timestamp when recorded", example = "2026-08-05T14:30:00Z")
    private Instant timestamp;
}
