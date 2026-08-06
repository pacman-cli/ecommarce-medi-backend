package com.example.ecommerce.delivery.dto.request;

import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for updating shipment status and logging timeline.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Shipment status update request payload")
public class UpdateDeliveryStatusRequest {

    @Schema(description = "New delivery status", example = "OUT_FOR_DELIVERY")
    @NotNull(message = "Delivery status is required")
    private DeliveryStatus status;

    @Schema(description = "Current location checkpoint", example = "Dhaka Central Hub")
    private String location;

    @Schema(description = "Update note or status comment", example = "Out with rider for final delivery")
    private String note;
}
