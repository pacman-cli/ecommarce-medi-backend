package com.example.ecommerce.audit.repository;

import com.example.ecommerce.audit.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link ActivityLog} entities.
 */
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long>, JpaSpecificationExecutor<ActivityLog> {

    Page<ActivityLog> findByIsAdminActivityTrue(Pageable pageable);

    List<ActivityLog> findByUserIdOrderByTimestampDesc(Long userId);

    long countByIsAdminActivityTrue();
}
