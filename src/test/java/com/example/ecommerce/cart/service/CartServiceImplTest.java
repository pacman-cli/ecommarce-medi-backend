package com.example.ecommerce.cart.service;

import com.example.ecommerce.cart.dto.request.AddToCartRequest;
import com.example.ecommerce.cart.dto.request.ApplyCouponRequest;
import com.example.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.cart.dto.response.CartItemResponse;
import com.example.ecommerce.cart.dto.response.CartResponse;
import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.entity.CartItem;
import com.example.ecommerce.cart.entity.CartStatus;
import com.example.ecommerce.cart.mapper.CartMapper;
import com.example.ecommerce.cart.repository.CartItemRepository;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.cart.service.impl.CartServiceImpl;
import com.example.ecommerce.cart.validator.CartValidator;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private CartValidator cartValidator;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private Product product;
    private CartItem cartItem;
    private CartResponse cartResponse;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("Paracetamol 500mg")
                .sku("MED-PARA-500")
                .sellingPrice(new BigDecimal("10.00"))
                .quantity(100)
                .status(ProductStatus.ACTIVE)
                .stockStatus(StockStatus.IN_STOCK)
                .active(true)
                .build();
        product.setId(200L);

        cart = Cart.builder()
                .sessionId("guest-123")
                .status(CartStatus.ACTIVE)
                .items(new ArrayList<>())
                .build();
        cart.setId(100L);

        cartItem = CartItem.builder()
                .id(10L)
                .cart(cart)
                .product(product)
                .quantity(2)
                .build();

        CartItemResponse itemResp = CartItemResponse.builder()
                .id(10L)
                .productId(200L)
                .productName("Paracetamol 500mg")
                .quantity(2)
                .unitPrice(new BigDecimal("10.00"))
                .totalPrice(new BigDecimal("20.00"))
                .build();

        cartResponse = CartResponse.builder()
                .id(100L)
                .sessionId("guest-123")
                .subtotal(new BigDecimal("20.00"))
                .grandTotal(new BigDecimal("25.00"))
                .items(List.of(itemResp))
                .status(CartStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("addToCart should validate stock, add line item, and recalculate cart totals")
    void addToCart_Success() {
        AddToCartRequest addReq = AddToCartRequest.builder()
                .productId(200L)
                .quantity(2)
                .sessionId("guest-123")
                .build();

        when(productRepository.findByIdAndDeletedFalse(200L)).thenReturn(Optional.of(product));
        when(cartRepository.findBySessionIdAndStatus("guest-123", CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
        doNothing().when(cartValidator).validateProductStock(product, 2);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toResponse(any(Cart.class))).thenReturn(cartResponse);

        CartResponse response = cartService.addToCart(addReq);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);

        verify(cartValidator).validateProductStock(product, 2);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("applyCoupon should apply valid code and update coupon discount")
    void applyCoupon_Success() {
        cart.addItem(cartItem);
        ApplyCouponRequest couponReq = ApplyCouponRequest.builder().couponCode("SAVE10").build();

        when(cartRepository.findBySessionIdAndStatus("guest-123", CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toResponse(any(Cart.class))).thenReturn(cartResponse);

        CartResponse response = cartService.applyCoupon(couponReq, "guest-123");

        assertThat(response).isNotNull();
        assertThat(cart.getCouponCode()).isEqualTo("SAVE10");
        assertThat(cart.getCouponDiscount()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("applyCoupon should throw BadRequestException if code is invalid")
    void applyCoupon_InvalidCode_ThrowsException() {
        ApplyCouponRequest couponReq = ApplyCouponRequest.builder().couponCode("INVALID_CODE").build();
        when(cartRepository.findBySessionIdAndStatus("guest-123", CartStatus.ACTIVE)).thenReturn(Optional.of(cart));

        assertThatThrownBy(() -> cartService.applyCoupon(couponReq, "guest-123"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid or expired coupon code");
    }

    @Test
    @DisplayName("clearCart should remove all items and reset totals")
    void clearCart_Success() {
        cart.addItem(cartItem);
        when(cartRepository.findBySessionIdAndStatus("guest-123", CartStatus.ACTIVE)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toResponse(any(Cart.class))).thenReturn(cartResponse);

        cartService.clearCart("guest-123");

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getSubtotal()).isEqualTo(BigDecimal.ZERO);
        assertThat(cart.getGrandTotal()).isEqualTo(BigDecimal.ZERO);
    }
}
