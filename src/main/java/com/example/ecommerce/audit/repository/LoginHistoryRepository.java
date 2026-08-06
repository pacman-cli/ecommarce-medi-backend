package com.example.ecommerce.audit.repository;

import com.example.ecommerce.audit.entity.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access repository for {@link LoginHistory} entities.
 */
@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long>, JpaSpecificationExecutor<LoginHistory> {

    List<LoginHistory> findByUserEmailOrderByTimestampDesc(String userEmail);

    Page<LoginHistory> findBySuccessFalse(Pageable pageable);

    long countBySuccessFalse();
}
