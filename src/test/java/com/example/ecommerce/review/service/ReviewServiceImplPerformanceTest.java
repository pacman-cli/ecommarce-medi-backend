package com.example.ecommerce.review.service;

import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderItem;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.review.mapper.ReviewMapper;
import com.example.ecommerce.review.repository.ReviewRepository;
import com.example.ecommerce.review.service.impl.ReviewServiceImpl;
import com.example.ecommerce.review.validator.ReviewValidator;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplPerformanceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ReviewMapper reviewMapper;
    @Mock
    private ReviewValidator reviewValidator;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private List<Order> mockOrders;

    @BeforeEach
    void setUp() {
        mockOrders = new ArrayList<>();
        // Generate a large number of orders to simulate a realistic database size
        for (long i = 1; i <= 10000; i++) {
            User user = new User();
            user.setId(i % 100); // 100 different users

            Order order = new Order();
            order.setUser(user);
            order.setStatus(OrderStatus.DELIVERED);

            List<OrderItem> items = new ArrayList<>();
            for(int j = 1; j <= 5; j++) {
               Product p = new Product();
               p.setId((i * 5 + j) % 500); // 500 different products
               OrderItem item = new OrderItem();
               item.setProduct(p);
               items.add(item);
            }
            order.setItems(items);
            mockOrders.add(order);
        }
    }

    @Test
    void testCheckVerifiedPurchasePerformance() {
        when(orderRepository.existsByUserIdAndItemsProductIdAndStatus(50L, 250L, OrderStatus.DELIVERED)).thenReturn(true);

        long targetUserId = 50L;
        long targetProductId = 250L;

        // Warmup
        for (int i = 0; i < 1000; i++) {
            ReflectionTestUtils.invokeMethod(reviewService, "checkVerifiedPurchase", targetUserId, targetProductId);
        }

        long startTime = System.nanoTime();

        int iterations = 1000;
        for (int i = 0; i < iterations; i++) {
            ReflectionTestUtils.invokeMethod(reviewService, "checkVerifiedPurchase", targetUserId, targetProductId);
        }

        long endTime = System.nanoTime();

        double avgTimeMs = (endTime - startTime) / 1_000_000.0 / iterations;
        System.out.println("Average execution time for checkVerifiedPurchase (10000 orders): " + avgTimeMs + " ms");
    }
}
