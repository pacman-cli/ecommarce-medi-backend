package com.example.ecommerce.cart.service.impl;

import com.example.ecommerce.cart.dto.request.AddToCartRequest;
import com.example.ecommerce.cart.dto.request.ApplyCouponRequest;
import com.example.ecommerce.cart.dto.request.MergeCartRequest;
import com.example.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.cart.dto.response.CartResponse;
import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.entity.CartItem;
import com.example.ecommerce.cart.entity.CartStatus;
import com.example.ecommerce.cart.mapper.CartMapper;
import com.example.ecommerce.cart.repository.CartItemRepository;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.cart.validator.CartValidator;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation managing shopping cart state, guest/user carts, stock validation,
 * price calculations, promotional coupon application and guest cart merging.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final CartValidator cartValidator;

    @Override
    @Transactional
    public CartResponse getOrCreateCart(String sessionId) {
        Cart cart = getActiveCartEntity(sessionId);
        return cartMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {
        log.info("Adding product ID {} (qty {}) to cart", request.getProductId(), request.getQuantity());
        Product product = productRepository.findByIdAndDeletedFalse(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));

        Cart cart = getActiveCartEntity(request.getSessionId());

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        int targetQuantity = request.getQuantity();
        if (existingItemOpt.isPresent()) {
            targetQuantity += existingItemOpt.get().getQuantity();
        }

        cartValidator.validateProductStock(product, targetQuantity);

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(targetQuantity);
            existingItem.recalculatePrices();
        } else {
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.addItem(newItem);
        }

        cart.recalculateTotals();
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(Long itemId, UpdateCartItemRequest request, String sessionId) {
        log.info("Updating cart item ID {} to quantity {}", itemId, request.getQuantity());
        Cart cart = getActiveCartEntity(sessionId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + itemId));

        cartValidator.validateProductStock(item.getProduct(), request.getQuantity());

        item.setQuantity(request.getQuantity());
        item.recalculatePrices();
        cart.recalculateTotals();

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse removeCartItem(Long itemId, String sessionId) {
        log.info("Removing cart item ID {}", itemId);
        Cart cart = getActiveCartEntity(sessionId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + itemId));

        cart.removeItem(item);
        cart.recalculateTotals();

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse applyCoupon(ApplyCouponRequest request, String sessionId) {
        log.info("Applying coupon code '{}'", request.getCouponCode());
        Cart cart = getActiveCartEntity(sessionId);

        String code = request.getCouponCode().trim().toUpperCase();
        BigDecimal discount = calculateCouponDiscount(code, cart.getSubtotal());

        cart.setCouponCode(code);
        cart.setCouponDiscount(discount);
        cart.recalculateTotals();

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse removeCoupon(String sessionId) {
        log.info("Removing coupon from cart");
        Cart cart = getActiveCartEntity(sessionId);
        cart.setCouponCode(null);
        cart.setCouponDiscount(BigDecimal.ZERO);
        cart.recalculateTotals();

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse clearCart(String sessionId) {
        log.info("Clearing all items from cart");
        Cart cart = getActiveCartEntity(sessionId);
        cart.clearItems();
        cart.setCouponCode(null);
        cart.setCouponDiscount(BigDecimal.ZERO);
        cart.recalculateTotals();

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponse mergeGuestCart(MergeCartRequest request) {
        User currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            throw new BadRequestException("Must be authenticated to merge a guest cart");
        }

        log.info("Merging guest session cart '{}' into user cart (userID: {})", request.getGuestSessionId(), currentUser.getId());

        Optional<Cart> guestCartOpt = cartRepository.findBySessionIdAndStatus(request.getGuestSessionId(), CartStatus.ACTIVE);
        Cart userCart = getOrCreateUserCart(currentUser);

        if (guestCartOpt.isPresent()) {
            Cart guestCart = guestCartOpt.get();
            if (!guestCart.getItems().isEmpty()) {
                for (CartItem guestItem : guestCart.getItems()) {
                    Product product = guestItem.getProduct();
                    Optional<CartItem> userItemOpt = userCart.getItems().stream()
                            .filter(i -> i.getProduct().getId().equals(product.getId()))
                            .findFirst();

                    int newQty = guestItem.getQuantity();
                    if (userItemOpt.isPresent()) {
                        newQty += userItemOpt.get().getQuantity();
                    }

                    try {
                        cartValidator.validateProductStock(product, newQty);
                        if (userItemOpt.isPresent()) {
                            userItemOpt.get().setQuantity(newQty);
                            userItemOpt.get().recalculatePrices();
                        } else {
                            CartItem newItem = CartItem.builder()
                                    .product(product)
                                    .quantity(guestItem.getQuantity())
                                    .build();
                            userCart.addItem(newItem);
                        }
                    } catch (BadRequestException e) {
                        log.warn("Skipping or capping product ID {} during cart merge due to stock limit: {}", product.getId(), e.getMessage());
                    }
                }
            }

            guestCart.setStatus(CartStatus.MERGED);
            cartRepository.save(guestCart);
        }

        userCart.recalculateTotals();
        Cart savedUserCart = cartRepository.save(userCart);
        return cartMapper.toResponse(savedUserCart);
    }

    private Cart getActiveCartEntity(String sessionId) {
        User currentUser = getCurrentUserEntity();
        if (currentUser != null) {
            return getOrCreateUserCart(currentUser);
        }

        String validSessionId = StringUtils.hasText(sessionId) ? sessionId : UUID.randomUUID().toString();
        return cartRepository.findBySessionIdAndStatus(validSessionId, CartStatus.ACTIVE)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .sessionId(validSessionId)
                        .status(CartStatus.ACTIVE)
                        .build()));
    }

    private Cart getOrCreateUserCart(User user) {
        return cartRepository.findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .user(user)
                        .status(CartStatus.ACTIVE)
                        .build()));
    }

    private User getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            return userRepository.findByEmailIgnoreCase(email).orElse(null);
        }
        return null;
    }

    private BigDecimal calculateCouponDiscount(String code, BigDecimal subtotal) {
        if ("SAVE10".equalsIgnoreCase(code)) {
            return subtotal.multiply(new BigDecimal("0.10")).setScale(2, java.math.RoundingMode.HALF_UP);
        } else if ("SAVE20".equalsIgnoreCase(code)) {
            return subtotal.multiply(new BigDecimal("0.20")).setScale(2, java.math.RoundingMode.HALF_UP);
        } else if ("FLAT5".equalsIgnoreCase(code)) {
            return new BigDecimal("5.00");
        } else {
            throw new BadRequestException("Invalid or expired coupon code: " + code);
        }
    }
}
