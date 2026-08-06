package com.example.ecommerce.dashboard.mapper;

import com.example.ecommerce.dashboard.dto.response.LowStockProductResponse;
import com.example.ecommerce.dashboard.dto.response.RecentOrderResponse;
import com.example.ecommerce.dashboard.dto.response.TopCategoryResponse;
import com.example.ecommerce.dashboard.dto.response.TopCustomerResponse;
import com.example.ecommerce.dashboard.dto.response.TopSellingProductResponse;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.product.entity.Product;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for converting dashboard entity projections and aggregates into DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface DashboardMapper {

    /**
     * Maps an {@link Order} domain entity to a {@link RecentOrderResponse} snapshot DTO.
     */
    default RecentOrderResponse toRecentOrderResponse(Order order) {
        if (order == null) {
            return null;
        }

        String customerName = "Guest Customer";
        String customerEmail = "guest@example.com";
        if (order.getUser() != null) {
            customerName = (order.getUser().getFirstName() + " " + order.getUser().getLastName()).trim();
            customerEmail = order.getUser().getEmail();
        } else if (order.getShippingAddress() != null) {
            customerName = order.getShippingAddress().getRecipientName();
        }

        return RecentOrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerName(customerName)
                .customerEmail(customerEmail)
                .itemCount(order.getItems() != null ? order.getItems().size() : 0)
                .grandTotal(order.getGrandTotal())
                .orderStatus(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    /**
     * Maps a {@link Product} entity to a {@link LowStockProductResponse} alert DTO.
     */
    default LowStockProductResponse toLowStockProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        return LowStockProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : "Uncategorized")
                .currentStock(product.getQuantity())
                .minStockThreshold(product.getLowStock())
                .price(product.getSellingPrice())
                .stockStatus(product.getStockStatus())
                .thumbnail(product.getThumbnail())
                .build();
    }

    /**
     * Converts raw JPA projection Object[] into {@link TopSellingProductResponse}.
     */
    default TopSellingProductResponse toTopSellingProductResponse(Object[] row) {
        if (row == null || row.length < 7) {
            return null;
        }

        return TopSellingProductResponse.builder()
                .productId((Long) row[0])
                .productName((String) row[1])
                .sku((String) row[2])
                .categoryName(row[3] != null ? (String) row[3] : "Uncategorized")
                .totalQuantitySold(row[4] != null ? ((Number) row[4]).longValue() : 0L)
                .totalRevenue(row[5] != null ? (BigDecimal) row[5] : BigDecimal.ZERO)
                .thumbnail((String) row[6])
                .build();
    }

    /**
     * Converts raw JPA projection Object[] into {@link TopCategoryResponse}.
     */
    default TopCategoryResponse toTopCategoryResponse(Object[] row, BigDecimal totalStoreRevenue) {
        if (row == null || row.length < 6) {
            return null;
        }

        BigDecimal categoryRevenue = row[4] != null ? (BigDecimal) row[4] : BigDecimal.ZERO;
        double sharePercentage = 0.0;
        if (totalStoreRevenue != null && totalStoreRevenue.compareTo(BigDecimal.ZERO) > 0) {
            sharePercentage = categoryRevenue.multiply(BigDecimal.valueOf(100))
                    .divide(totalStoreRevenue, 2, java.math.RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return TopCategoryResponse.builder()
                .categoryId((Long) row[0])
                .categoryName((String) row[1])
                .orderCount(row[2] != null ? ((Number) row[2]).longValue() : 0L)
                .totalUnitsSold(row[3] != null ? ((Number) row[3]).longValue() : 0L)
                .totalRevenue(categoryRevenue)
                .sharePercentage(sharePercentage)
                .categoryImage((String) row[5])
                .build();
    }

    /**
     * Converts raw JPA projection Object[] into {@link TopCustomerResponse}.
     */
    default TopCustomerResponse toTopCustomerResponse(Object[] row) {
        if (row == null || row.length < 8) {
            return null;
        }

        String fullName = ((String) row[1] + " " + (String) row[2]).trim();

        return TopCustomerResponse.builder()
                .userId((Long) row[0])
                .fullName(fullName)
                .email((String) row[3])
                .phone((String) row[4])
                .totalOrders(row[5] != null ? ((Number) row[5]).longValue() : 0L)
                .totalSpent(row[6] != null ? (BigDecimal) row[6] : BigDecimal.ZERO)
                .lastOrderDate((Instant) row[7])
                .build();
    }

    default List<RecentOrderResponse> toRecentOrderResponseList(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream()
                .map(this::toRecentOrderResponse)
                .collect(Collectors.toList());
    }

    default List<LowStockProductResponse> toLowStockProductResponseList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }
        return products.stream()
                .map(this::toLowStockProductResponse)
                .collect(Collectors.toList());
    }
}
