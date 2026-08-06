package com.example.ecommerce.payment.repository;

import com.example.ecommerce.payment.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link PaymentTransaction} audit logs.
 */
@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    List<PaymentTransaction> findByPaymentIdOrderByCreatedAtDesc(Long paymentId);
}
