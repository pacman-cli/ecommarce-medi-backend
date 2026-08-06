package com.example.ecommerce.delivery.repository;

import com.example.ecommerce.delivery.entity.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link DeliveryPartner} carrier entities.
 */
@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {

    Optional<DeliveryPartner> findByIdAndDeletedFalse(Long id);

    Optional<DeliveryPartner> findByCodeAndDeletedFalse(String code);

    List<DeliveryPartner> findByActiveTrueAndDeletedFalse();

    boolean existsByCodeAndDeletedFalse(String code);
}
