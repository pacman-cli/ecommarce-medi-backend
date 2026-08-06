package com.example.ecommerce.inventory.repository;

import com.example.ecommerce.inventory.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link Warehouse} entities.
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {

    Optional<Warehouse> findByIdAndDeletedFalse(Long id);

    Optional<Warehouse> findByCodeAndDeletedFalse(String code);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    List<Warehouse> findByActiveTrueAndDeletedFalse();
}
