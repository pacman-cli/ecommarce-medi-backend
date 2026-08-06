package com.example.ecommerce.audit.entity;

import com.example.ecommerce.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link AuditLog} entities.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

    List<AuditLog> findByEntityNameAndEntityIdOrderByTimestampDesc(String entityName, String entityId);

    List<AuditLog> findByUserIdOrderByTimestampDesc(Long userId);

    long countByEntityName(String entityName);
}
