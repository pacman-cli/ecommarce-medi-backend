package com.example.ecommerce.security;

import com.example.ecommerce.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring Security adapter wrapping the application {@link User} entity.
 *
 * <p>Keeps the entity itself free of framework dependencies and exposes the
 * role as a {@code ROLE_*} authority expected by {@code hasRole()} checks.</p>
 */
public class UserPrincipal implements UserDetails {

    @Getter
    private final User user;

    /**
     * Creates a principal from a user entity.
     *
     * @param user the persisted user
     */
    public UserPrincipal(User user) {
        this.user = user;
    }

    /**
     * Static factory for readability.
     *
     * @param user the persisted user
     * @return the principal
     */
    public static UserPrincipal create(User user) {
        return new UserPrincipal(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        // Granular permissions modelled on the Role -> Permission mapping so that
        // @PreAuthorize("hasAuthority('PROFILE_READ')") etc. actually resolve.
        user.getRole().getPermissions()
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.name())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
