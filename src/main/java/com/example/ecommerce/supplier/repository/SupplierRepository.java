package com.example.ecommerce.supplier.repository;

import com.example.ecommerce.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link Supplier} entities supporting JPA Specification queries.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {

    Optional<Supplier> findByIdAndDeletedFalse(Long id);

    Optional<Supplier> findByCodeAndDeletedFalse(String code);

    Optional<Supplier> findByNameAndDeletedFalse(String name);

    boolean existsByCodeAndDeletedFalse(String code);

    boolean existsByNameAndDeletedFalse(String name);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    List<Supplier> findByActiveTrueAndDeletedFalse();
}
