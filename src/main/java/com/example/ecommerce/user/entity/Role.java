package com.example.ecommerce.user.entity;

import java.util.EnumSet;
import java.util.Set;

/**
 * Application roles and the permissions each role carries.
 *
 * <p>Authorization is expressed both through the role itself (as a
 * {@code ROLE_*} authority) and through the granular permissions listed here.</p>
 */
public enum Role {

    /** Default role assigned at registration. */
    CUSTOMER(EnumSet.of(
            Permission.PROFILE_READ,
            Permission.PROFILE_WRITE,
            Permission.ADDRESS_READ,
            Permission.ADDRESS_WRITE,
            Permission.PRODUCT_READ,
            Permission.ORDER_READ,
            Permission.ORDER_WRITE)),

    /** Content moderator without user administration rights. */
    MODERATOR(EnumSet.of(
            Permission.PROFILE_READ,
            Permission.PROFILE_WRITE,
            Permission.ADDRESS_READ,
            Permission.ADDRESS_WRITE,
            Permission.PRODUCT_READ,
            Permission.PRODUCT_WRITE,
            Permission.CATEGORY_READ,
            Permission.CATEGORY_WRITE,
            Permission.ORDER_READ)),

    /** Full administration rights. */
    ADMIN(EnumSet.allOf(Permission.class));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = Set.copyOf(permissions);
    }

    /**
     * Returns the permissions granted by this role.
     *
     * @return the immutable permission set
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }
}
