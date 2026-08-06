package com.example.ecommerce.delivery.repository;

import com.example.ecommerce.delivery.entity.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link DeliveryZone} geographic area entities.
 */
@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {

    Optional<DeliveryZone> findByIdAndDeletedFalse(Long id);

    Optional<DeliveryZone> findByCodeAndDeletedFalse(String code);

    List<DeliveryZone> findByActiveTrueAndDeletedFalse();

    @Query("SELECT z FROM DeliveryZone z WHERE z.active = true AND z.deleted = false " +
           "AND (:division IS NULL OR LOWER(z.division) = LOWER(:division)) " +
           "AND (:district IS NULL OR LOWER(z.district) = LOWER(:district))")
    Optional<DeliveryZone> findMatchingZone(@Param("division") String division, @Param("district") String district);

    boolean existsByCodeAndDeletedFalse(String code);
}
