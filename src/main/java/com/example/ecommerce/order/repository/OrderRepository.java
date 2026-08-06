package com.example.ecommerce.order.repository;

import com.example.ecommerce.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access repository for {@link Order} aggregates.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByIdAndDeletedFalse(Long id);

    Optional<Order> findByOrderNumberAndDeletedFalse(String orderNumber);

    Optional<Order> findByInvoiceNumberAndDeletedFalse(String invoiceNumber);

    Page<Order> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Optional<Order> findByIdAndUserIdAndDeletedFalse(Long id, Long userId);
}
