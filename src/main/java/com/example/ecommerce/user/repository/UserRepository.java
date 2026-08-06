package com.example.ecommerce.user.repository;

import com.example.ecommerce.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Data access for {@link User} aggregates.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Finds a user by exact email.
     *
     * @param email the email address
     * @return the user, if present
     */
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Checks whether an account already exists for the email (case-insensitive).
     *
     * @param email the email address
     * @return {@code true} when the email is taken
     */
    boolean existsByEmailIgnoreCase(String email);

    @org.springframework.data.jpa.repository.Query("""
            SELECT u FROM User u
            WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<User> search(@org.springframework.data.repository.query.Param("keyword") String keyword, Pageable pageable);

    /**
     * Checks whether the email is taken by a different user.
     *
     * @param email the candidate email
     * @param id    the user to exclude
     * @return {@code true} when taken by another user
     */
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    /**
     * {@inheritDoc}
     *
     * <p>Overridden with an {@link EntityGraph} so listing queries eagerly fetch
     * the address collection and avoid N+1 selects.</p>
     */
    @Override
    @EntityGraph(attributePaths = "addresses")
    Page<User> findAll(Pageable pageable);
}
