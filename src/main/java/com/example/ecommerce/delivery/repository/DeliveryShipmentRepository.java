package com.example.ecommerce.delivery.repository;

import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
import com.example.ecommerce.delivery.entity.DeliveryShipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link DeliveryShipment} order aggregates.
 */
@Repository
public interface DeliveryShipmentRepository extends JpaRepository<DeliveryShipment, Long> {

    Optional<DeliveryShipment> findByIdAndDeletedFalse(Long id);

    Optional<DeliveryShipment> findByTrackingNumberAndDeletedFalse(String trackingNumber);

    Optional<DeliveryShipment> findByShipmentNumberAndDeletedFalse(String shipmentNumber);

    Optional<DeliveryShipment> findByOrderIdAndDeletedFalse(Long orderId);

    Page<DeliveryShipment> findByStatusAndDeletedFalse(DeliveryStatus status, Pageable pageable);

    List<DeliveryShipment> findByOrderIdInAndDeletedFalse(List<Long> orderIds);
}
