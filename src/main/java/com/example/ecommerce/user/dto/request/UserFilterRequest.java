package com.example.ecommerce.user.dto.request;

import com.example.ecommerce.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dynamic query-parameter payload used to filter the user listing.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterRequest {

    /** Free-text match against email, first or last name. */
    private String search;

    /** Restrict to a single role. */
    private Role role;

    /** Restrict by account status. */
    private Boolean enabled;
}
