package com.example.ecommerce.order.repository;

import com.example.ecommerce.order.entity.OrderTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link OrderTimeline} transition logs.
 */
@Repository
public interface OrderTimelineRepository extends JpaRepository<OrderTimeline, Long> {

    List<OrderTimeline> findByOrderIdOrderByChangedAtAsc(Long orderId);
}
