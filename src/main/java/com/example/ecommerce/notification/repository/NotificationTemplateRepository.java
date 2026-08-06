package com.example.ecommerce.notification.repository;

import com.example.ecommerce.notification.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access repository for {@link NotificationTemplate} definitions.
 */
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByTemplateCodeAndDeletedFalse(String templateCode);

    boolean existsByTemplateCodeAndDeletedFalse(String templateCode);
}
