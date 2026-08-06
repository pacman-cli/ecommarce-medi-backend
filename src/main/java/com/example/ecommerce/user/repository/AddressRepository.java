package com.example.ecommerce.user.repository;

import com.example.ecommerce.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

/**
 * Data access for {@link Address} aggregates.
 */
@Repository("userAddressRepository")
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Returns all addresses of a user, default first, then most recent.
     *
     * @param userId the owning user id
     * @return the ordered addresses
     */
    List<Address> findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(Long userId);

    /**
     * Finds an address belonging to a specific user.
     *
     * @param id     the address id
     * @param userId the owning user id
     * @return the address, if it belongs to the user
     */
    Optional<Address> findByIdAndUserId(Long id, Long userId);

    /**
     * Clears the default flag on every address of a user.
     *
     * @param userId the owning user id
     * @return the number of updated rows
     */
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId AND a.isDefault = true")
    int clearDefaultForUser(@Param("userId") Long userId);
}
