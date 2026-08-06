package com.example.ecommerce.order.service.impl;

import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.entity.CartItem;
import com.example.ecommerce.cart.entity.CartStatus;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.coupon.service.CouponService;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.UnauthorizedException;
import com.example.ecommerce.order.dto.request.AddOrderNoteRequest;
import com.example.ecommerce.order.dto.request.CheckoutRequest;
import com.example.ecommerce.order.dto.request.OrderFilterRequest;
import com.example.ecommerce.order.dto.request.UpdateOrderStatusRequest;
import com.example.ecommerce.order.dto.response.InvoiceResponse;
import com.example.ecommerce.order.dto.response.OrderResponse;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderAddress;
import com.example.ecommerce.order.entity.OrderItem;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.entity.PaymentStatus;
import com.example.ecommerce.order.mapper.OrderMapper;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.order.specification.OrderSpecification;
import com.example.ecommerce.order.validator.OrderValidator;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Service implementation handling checkout order creation, inventory deduction, coupon recording,
 * status timeline progression, tracking number assignments, and invoice generation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;

    private static final Random RANDOM = new Random();

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutRequest request) {
        User currentUser = getCurrentUserEntity();
        Cart cart = resolveActiveCart(request.getSessionId(), currentUser);

        orderValidator.validateCartForCheckout(cart);

        OrderAddress shipping = orderMapper.toAddressEntity(request.getShippingAddress());
        OrderAddress billing = request.isSameAsShipping() || request.getBillingAddress() == null
                ? shipping
                : orderMapper.toAddressEntity(request.getBillingAddress());

        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String suffix = String.format("%05d", RANDOM.nextInt(100000));
        String orderNumber = "ORD-" + datePrefix + "-" + suffix;
        String invoiceNumber = "INV-" + datePrefix + "-" + suffix;

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .invoiceNumber(invoiceNumber)
                .user(currentUser)
                .shippingAddress(shipping)
                .billingAddress(billing)
                .subtotal(cart.getSubtotal())
                .itemDiscount(cart.getItemDiscount())
                .couponCode(cart.getCouponCode())
                .couponDiscount(cart.getCouponDiscount())
                .shippingFee(cart.getShippingCharge())
                .taxAmount(cart.getTaxAmount())
                .grandTotal(cart.getGrandTotal())
                .orderNotes(request.getOrderNotes())
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getQuantity() != null) {
                int remaining = product.getQuantity() - cartItem.getQuantity();
                if (remaining < 0) {
                    throw new BadRequestException("Insufficient stock for product: " + product.getName());
                }
                product.setQuantity(remaining);
                productRepository.save(product);
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .productName(product.getName())
                    .productSku(product.getSku())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getUnitPrice())
                    .discountPrice(cartItem.getDiscountPrice())
                    .taxAmount(cartItem.getTaxAmount())
                    .totalPrice(cartItem.getTotalPrice())
                    .build();
            order.addItem(orderItem);
        }

        String creator = currentUser != null ? currentUser.getEmail() : "Guest Customer";
        order.addTimeline(OrderStatus.PENDING, "Order placed successfully", creator);

        Order savedOrder = orderRepository.save(order);

        cart.setStatus(CartStatus.CONVERTED_TO_ORDER);
        cartRepository.save(cart);

        log.info("Successfully created order {} with ID {}", orderNumber, savedOrder.getId());
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        validateOrderAccess(order);
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumberAndDeletedFalse(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
        validateOrderAccess(order);
        return orderMapper.toResponse(order);
    }

    @Override
    public PageResponse<OrderResponse> getMyOrders(Pageable pageable) {
        User currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            throw new UnauthorizedException("User must be authenticated to view order history");
        }
        Page<Order> page = orderRepository.findByUserIdAndDeletedFalse(currentUser.getId(), pageable);
        return PageResponse.from(page, orderMapper::toResponse);
    }

    @Override
    public PageResponse<OrderResponse> getAllOrders(OrderFilterRequest filter, Pageable pageable) {
        Specification<Order> spec = OrderSpecification.build(filter, null);
        Page<Order> page = orderRepository.findAll(spec, pageable);
        return PageResponse.from(page, orderMapper::toResponse);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        orderValidator.validateStatusTransition(order.getStatus(), request.getStatus());

        order.setStatus(request.getStatus());

        if (StringUtils.hasText(request.getTrackingNumber())) {
            order.setTrackingNumber(request.getTrackingNumber());
        }

        if (request.getStatus() == OrderStatus.DELIVERED) {
            order.setDeliveredAt(Instant.now());
            order.setPaymentStatus(PaymentStatus.PAID);
        } else if (request.getStatus() == OrderStatus.CANCELLED) {
            order.setCancelledAt(Instant.now());
            restoreInventory(order);
        }

        String updatedBy = getCurrentUserEmail();
        String note = StringUtils.hasText(request.getNote()) ? request.getNote() : "Status updated to " + request.getStatus();
        order.addTimeline(request.getStatus(), note, updatedBy);

        Order saved = orderRepository.save(order);
        log.info("Updated order ID {} status to {}", id, request.getStatus());
        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id, String reason) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        validateOrderAccess(order);
        orderValidator.validateStatusTransition(order.getStatus(), OrderStatus.CANCELLED);

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(Instant.now());
        order.setCancelReason(reason);

        restoreInventory(order);

        String actor = getCurrentUserEmail();
        order.addTimeline(OrderStatus.CANCELLED, "Order cancelled. Reason: " + (reason != null ? reason : "Customer request"), actor);

        Order saved = orderRepository.save(order);
        log.info("Cancelled order ID {} with reason: {}", id, reason);
        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public OrderResponse addOrderNote(Long id, AddOrderNoteRequest request) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        String actor = getCurrentUserEmail();
        order.addTimeline(order.getStatus(), "Note added: " + request.getNote(), actor);

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Override
    public InvoiceResponse getOrderInvoice(Long id) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        validateOrderAccess(order);
        return orderMapper.toInvoiceResponse(order);
    }

    private Cart resolveActiveCart(String sessionId, User user) {
        if (user != null) {
            return cartRepository.findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE)
                    .orElseThrow(() -> new BadRequestException("No active shopping cart found for user"));
        } else if (StringUtils.hasText(sessionId)) {
            return cartRepository.findBySessionIdAndStatus(sessionId, CartStatus.ACTIVE)
                    .orElseThrow(() -> new BadRequestException("No active shopping cart found for session ID: " + sessionId));
        } else {
            throw new BadRequestException("Session ID or authenticated user is required for checkout");
        }
    }

    private void restoreInventory(Order order) {
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                if (product != null && product.getQuantity() != null) {
                    product.setQuantity(product.getQuantity() + item.getQuantity());
                    productRepository.save(product);
                }
            }
        }
    }

    private void validateOrderAccess(Order order) {
        User currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            return;
        }
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && (order.getUser() == null || !order.getUser().getId().equals(currentUser.getId()))) {
            throw new UnauthorizedException("You do not have permission to access this order");
        }
    }

    private User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            return userRepository.findByEmailIgnoreCase(email).orElse(null);
        }
        return null;
    }

    private String getCurrentUserEmail() {
        User user = getCurrentUserEntity();
        return user != null ? user.getEmail() : "System";
    }
}
