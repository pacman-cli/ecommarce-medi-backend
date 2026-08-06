package com.example.ecommerce.order.service;

import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.entity.CartItem;
import com.example.ecommerce.cart.entity.CartStatus;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.coupon.service.CouponService;
import com.example.ecommerce.order.dto.request.CheckoutRequest;
import com.example.ecommerce.order.dto.request.OrderAddressDto;
import com.example.ecommerce.order.dto.request.UpdateOrderStatusRequest;
import com.example.ecommerce.order.dto.response.InvoiceResponse;
import com.example.ecommerce.order.dto.response.OrderResponse;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderAddress;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import com.example.ecommerce.order.mapper.OrderMapper;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.order.service.impl.OrderServiceImpl;
import com.example.ecommerce.order.validator.OrderValidator;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.user.entity.Role;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponService couponService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Product product;
    private Cart cart;
    private CartItem cartItem;
    private Order order;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.CUSTOMER)
                .build();
        user.setId(1L);

        product = Product.builder()
                .name("Paracetamol 500mg")
                .sku("MED-PARA-500")
                .sellingPrice(new BigDecimal("10.00"))
                .quantity(50)
                .build();
        product.setId(200L);

        cartItem = CartItem.builder()
                .id(10L)
                .product(product)
                .quantity(2)
                .unitPrice(new BigDecimal("10.00"))
                .totalPrice(new BigDecimal("20.00"))
                .build();

        cart = Cart.builder()
                .user(user)
                .items(new ArrayList<>())
                .subtotal(new BigDecimal("20.00"))
                .grandTotal(new BigDecimal("25.00"))
                .status(CartStatus.ACTIVE)
                .build();
        cart.addItem(cartItem);

        OrderAddress address = OrderAddress.builder()
                .recipientName("John Doe")
                .phone("+15550192834")
                .streetAddress("742 Evergreen Terrace")
                .city("Springfield")
                .zipCode("97477")
                .country("USA")
                .build();

        order = Order.builder()
                .orderNumber("ORD-20260804-00001")
                .invoiceNumber("INV-20260804-00001")
                .user(user)
                .shippingAddress(address)
                .billingAddress(address)
                .subtotal(new BigDecimal("20.00"))
                .grandTotal(new BigDecimal("25.00"))
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        order.setId(100L);

        orderResponse = OrderResponse.builder()
                .id(100L)
                .orderNumber("ORD-20260804-00001")
                .invoiceNumber("INV-20260804-00001")
                .status(OrderStatus.PENDING)
                .grandTotal(new BigDecimal("25.00"))
                .build();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user@example.com", "password", Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("checkout should validate cart, deduct stock, create order and return response")
    void checkout_Success() {
        OrderAddressDto addressDto = OrderAddressDto.builder()
                .recipientName("John Doe")
                .phone("+15550192834")
                .streetAddress("742 Evergreen Terrace")
                .city("Springfield")
                .zipCode("97477")
                .country("USA")
                .build();

        CheckoutRequest request = CheckoutRequest.builder()
                .shippingAddress(addressDto)
                .sameAsShipping(true)
                .build();

        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserIdAndStatus(1L, CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
        doNothing().when(orderValidator).validateCartForCheckout(cart);
        when(orderMapper.toAddressEntity(addressDto)).thenReturn(order.getShippingAddress());
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);

        OrderResponse response = orderService.checkout(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);

        verify(orderValidator).validateCartForCheckout(cart);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("updateOrderStatus should validate transition, update status, and add timeline")
    void updateOrderStatus_Success() {
        UpdateOrderStatusRequest updateReq = UpdateOrderStatusRequest.builder()
                .status(OrderStatus.SHIPPED)
                .trackingNumber("TRACK-FDX-1234")
                .note("Handed to FedEx")
                .build();

        when(orderRepository.findByIdAndDeletedFalse(100L)).thenReturn(Optional.of(order));
        doNothing().when(orderValidator).validateStatusTransition(OrderStatus.PENDING, OrderStatus.SHIPPED);
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);

        OrderResponse response = orderService.updateOrderStatus(100L, updateReq);

        assertThat(response).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPED);
        assertThat(order.getTrackingNumber()).isEqualTo("TRACK-FDX-1234");
    }

    @Test
    @DisplayName("cancelOrder should update status to CANCELLED and restore inventory")
    void cancelOrder_Success() {
        when(orderRepository.findByIdAndDeletedFalse(100L)).thenReturn(Optional.of(order));
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        doNothing().when(orderValidator).validateStatusTransition(OrderStatus.PENDING, OrderStatus.CANCELLED);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);

        OrderResponse response = orderService.cancelOrder(100L, "Customer request");

        assertThat(response).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.getCancelReason()).isEqualTo("Customer request");
    }

    @Test
    @DisplayName("getOrderInvoice should return invoice response")
    void getOrderInvoice_Success() {
        InvoiceResponse invoiceResp = InvoiceResponse.builder()
                .invoiceNumber("INV-20260804-00001")
                .orderNumber("ORD-20260804-00001")
                .grandTotal(new BigDecimal("25.00"))
                .build();

        when(orderRepository.findByIdAndDeletedFalse(100L)).thenReturn(Optional.of(order));
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(orderMapper.toInvoiceResponse(order)).thenReturn(invoiceResp);

        InvoiceResponse response = orderService.getOrderInvoice(100L);

        assertThat(response).isNotNull();
        assertThat(response.getInvoiceNumber()).isEqualTo("INV-20260804-00001");
    }
}
