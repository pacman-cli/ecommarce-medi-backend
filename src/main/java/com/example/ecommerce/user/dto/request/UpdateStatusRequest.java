package com.example.ecommerce.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for enabling or disabling a user account.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;
}
