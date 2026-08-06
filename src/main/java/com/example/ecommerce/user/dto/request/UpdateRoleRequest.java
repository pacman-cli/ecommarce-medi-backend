package com.example.ecommerce.user.dto.request;

import com.example.ecommerce.user.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for changing a user's role.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {

    @NotNull(message = "Role is required")
    private Role role;
}
