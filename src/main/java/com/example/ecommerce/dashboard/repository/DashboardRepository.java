package com.example.ecommerce.dashboard.repository;

import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import com.example.ecommerce.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Data access repository exposing aggregate analytical queries for the administrative dashboard.
 */
@Repository
public interface DashboardRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate")
    Long countOrdersBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(o.grandTotal), 0) FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED")
    BigDecimal sumGrossRevenueBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(o.subtotal), 0) FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED")
    BigDecimal sumNetRevenueBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(o.taxAmount), 0) FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED")
    BigDecimal sumTaxBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(o.shippingFee), 0) FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED")
    BigDecimal sumShippingBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(o.itemDiscount + o.couponDiscount), 0) FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED")
    BigDecimal sumDiscountsBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    Long countOrdersByStatusBetween(@Param("status") OrderStatus status, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :paymentStatus AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    Long countOrdersByPaymentStatusBetween(@Param("paymentStatus") PaymentStatus paymentStatus, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = com.example.ecommerce.user.entity.Role.CUSTOMER")
    Long countTotalCustomers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = com.example.ecommerce.user.entity.Role.CUSTOMER AND u.createdAt >= :startDate AND u.createdAt <= :endDate")
    Long countNewCustomersBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COUNT(DISTINCT o.user.id) FROM Order o WHERE o.user IS NOT NULL AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    Long countActiveCustomersBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT u.id, u.firstName, u.lastName, u.email, u.phone, COUNT(o), SUM(o.grandTotal), MAX(o.createdAt) " +
           "FROM Order o JOIN o.user u " +
           "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED " +
           "GROUP BY u.id, u.firstName, u.lastName, u.email, u.phone " +
           "ORDER BY SUM(o.grandTotal) DESC")
    List<Object[]> findTopCustomersBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate, Pageable pageable);

    @Query("SELECT p.id, p.name, p.sku, c.name, SUM(oi.quantity), SUM(oi.totalPrice), p.thumbnail " +
           "FROM OrderItem oi JOIN oi.order o JOIN oi.product p LEFT JOIN p.category c " +
           "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED " +
           "GROUP BY p.id, p.name, p.sku, c.name, p.thumbnail " +
           "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingProductsBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate, Pageable pageable);

    @Query("SELECT c.id, c.name, COUNT(DISTINCT o.id), SUM(oi.quantity), SUM(oi.totalPrice), c.categoryImage " +
           "FROM OrderItem oi JOIN oi.order o JOIN oi.product p JOIN p.category c " +
           "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED " +
           "GROUP BY c.id, c.name, c.categoryImage " +
           "ORDER BY SUM(oi.totalPrice) DESC")
    List<Object[]> findTopCategoriesBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.deleted = false AND (p.quantity <= p.lowStock OR p.stockStatus = com.example.ecommerce.product.entity.StockStatus.LOW_STOCK OR p.stockStatus = com.example.ecommerce.product.entity.StockStatus.OUT_OF_STOCK) ORDER BY p.quantity ASC")
    List<Product> findLowStockProducts(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.deleted = false AND (p.quantity <= p.lowStock OR p.stockStatus = com.example.ecommerce.product.entity.StockStatus.LOW_STOCK)")
    Long countLowStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.deleted = false AND (p.quantity = 0 OR p.stockStatus = com.example.ecommerce.product.entity.StockStatus.OUT_OF_STOCK)")
    Long countOutOfStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.deleted = false AND p.quantity > p.lowStock AND p.stockStatus = com.example.ecommerce.product.entity.StockStatus.IN_STOCK")
    Long countInStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.deleted = false")
    Long countTotalProducts();

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Product p WHERE p.deleted = false")
    Long sumTotalQuantityInStock();

    @Query("SELECT COALESCE(SUM(p.quantity * p.sellingPrice), 0) FROM Product p WHERE p.deleted = false")
    BigDecimal sumTotalInventoryValue();

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.deleted = false ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(Pageable pageable);

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi JOIN oi.order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.status != com.example.ecommerce.order.entity.OrderStatus.CANCELLED")
    Long sumUnitsSoldBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate AND o.deleted = false ORDER BY o.createdAt ASC")
    List<Order> findOrdersBetweenForTrends(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}
