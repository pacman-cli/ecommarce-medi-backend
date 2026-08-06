package com.example.ecommerce.security;

import com.example.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Resolves {@link UserDetails} from the persistence layer using the email as
 * the login identifier.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by email and adapts it to a {@link UserPrincipal}.
     *
     * @param email the login email
     * @return the user details
     * @throws UsernameNotFoundException when no matching user exists
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserPrincipal::create)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
    }
}
