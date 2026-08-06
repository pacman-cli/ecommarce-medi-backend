package com.example.ecommerce.search.repository;

import com.example.ecommerce.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface providing specification-driven product search execution.
 */
@Repository
public interface SearchProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT MIN(p.sellingPrice) FROM Product p WHERE p.deleted = false AND p.active = true")
    BigDecimal findOverallMinPrice();

    @Query("SELECT MAX(p.sellingPrice) FROM Product p WHERE p.deleted = false AND p.active = true")
    BigDecimal findOverallMaxPrice();

    @Query("SELECT c.name, COUNT(p) FROM Product p JOIN p.category c WHERE p.deleted = false AND p.active = true GROUP BY c.name")
    List<Object[]> countProductsByCategory();

    @Query("SELECT b.name, COUNT(p) FROM Product p JOIN p.brand b WHERE p.deleted = false AND p.active = true GROUP BY b.name")
    List<Object[]> countProductsByBrand();

    @Query("SELECT p.name FROM Product p WHERE p.deleted = false AND p.active = true AND LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<String> findMatchingProductNames(@Param("query") String query, Pageable pageable);
}
