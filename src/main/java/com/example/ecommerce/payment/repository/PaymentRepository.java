package com.example.ecommerce.payment.repository;

import com.example.ecommerce.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access repository for {@link Payment} entities.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    Optional<Payment> findByTransactionIdAndDeletedFalse(String transactionId);

    Optional<Payment> findByOrderIdAndDeletedFalse(Long orderId);

    Optional<Payment> findByGatewayTransactionIdAndDeletedFalse(String gatewayTransactionId);
}
