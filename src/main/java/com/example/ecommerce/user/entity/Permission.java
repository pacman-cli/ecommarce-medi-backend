package com.example.ecommerce.user.entity;

/**
 * Fine-grained authorities that can be granted to roles.
 *
 * <p>Permissions are emitted as Spring Security authorities alongside the
 * {@code ROLE_*} authority, enabling {@code @PreAuthorize("hasAuthority(...)")}
 * style authorization at the action level.</p>
 */
public enum Permission {

    /** View own profile. */
    PROFILE_READ,

    /** Edit own profile. */
    PROFILE_WRITE,

    /** Manage own addresses. */
    ADDRESS_READ,

    /** Edit own addresses. */
    ADDRESS_WRITE,

    /** Browse products. */
    PRODUCT_READ,

    /** Create/edit/deactivate products. */
    PRODUCT_WRITE,

    /** Browse categories. */
    CATEGORY_READ,

    /** Create/edit/deactivate categories. */
    CATEGORY_WRITE,

    /** View orders. */
    ORDER_READ,

    /** Create/update orders. */
    ORDER_WRITE,

    /** View and manage other users. */
    USER_READ,

    /** Modify other users' roles and status. */
    USER_WRITE,

    /** Unrestricted administration. */
    ADMIN_ALL
}
