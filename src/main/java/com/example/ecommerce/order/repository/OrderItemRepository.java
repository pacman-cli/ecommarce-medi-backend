package com.example.ecommerce.order.repository;

import com.example.ecommerce.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link OrderItem} line item snapshots.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);
}
