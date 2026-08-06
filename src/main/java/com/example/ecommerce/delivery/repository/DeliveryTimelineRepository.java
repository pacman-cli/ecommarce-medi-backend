package com.example.ecommerce.delivery.repository;

import com.example.ecommerce.delivery.entity.DeliveryTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link DeliveryTimeline} update logs.
 */
@Repository
public interface DeliveryTimelineRepository extends JpaRepository<DeliveryTimeline, Long> {

    List<DeliveryTimeline> findByShipmentIdOrderByTimestampAsc(Long shipmentId);
}
