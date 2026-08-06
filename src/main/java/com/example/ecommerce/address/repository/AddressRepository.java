package com.example.ecommerce.address.repository;

import com.example.ecommerce.user.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for managing customer {@link Address} entities.
 */
@Repository("customerAddressModuleRepository")
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByIdAndDeletedFalse(Long id);

    Optional<Address> findByIdAndUserIdAndDeletedFalse(Long id, Long userId);

    Page<Address> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<Address> findByUserIdAndDeletedFalse(Long userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.defaultShipping = true AND a.deleted = false")
    Optional<Address> findDefaultShippingAddressByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.defaultBilling = true AND a.deleted = false")
    Optional<Address> findDefaultBillingAddressByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Address a SET a.defaultShipping = false, a.isDefault = false WHERE a.user.id = :userId")
    void unsetPreviousDefaultShipping(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Address a SET a.defaultBilling = false WHERE a.user.id = :userId")
    void unsetPreviousDefaultBilling(@Param("userId") Long userId);

    long countByUserIdAndDeletedFalse(Long userId);
}
