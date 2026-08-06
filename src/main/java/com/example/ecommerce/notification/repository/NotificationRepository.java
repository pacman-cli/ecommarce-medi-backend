package com.example.ecommerce.notification.repository;

import com.example.ecommerce.notification.entity.Notification;
import com.example.ecommerce.notification.entity.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access repository for {@link Notification} logs.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    Optional<Notification> findByIdAndDeletedFalse(Long id);

    Page<Notification> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    long countByUserIdAndStatusAndDeletedFalse(Long userId, NotificationStatus status);
}
