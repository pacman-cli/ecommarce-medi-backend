package com.example.ecommerce.wishlist.service;

import com.example.ecommerce.cart.dto.response.CartResponse;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.user.entity.Role;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import com.example.ecommerce.wishlist.dto.response.WishlistCountResponse;
import com.example.ecommerce.wishlist.dto.response.WishlistResponse;
import com.example.ecommerce.wishlist.entity.Wishlist;
import com.example.ecommerce.wishlist.entity.WishlistItem;
import com.example.ecommerce.wishlist.mapper.WishlistMapper;
import com.example.ecommerce.wishlist.repository.WishlistItemRepository;
import com.example.ecommerce.wishlist.repository.WishlistRepository;
import com.example.ecommerce.wishlist.service.impl.WishlistServiceImpl;
import com.example.ecommerce.wishlist.validator.WishlistValidator;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistItemRepository wishlistItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    @Mock
    private WishlistMapper wishlistMapper;

    @Mock
    private WishlistValidator wishlistValidator;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private User user;
    private Product product;
    private Wishlist wishlist;
    private WishlistItem wishlistItem;
    private WishlistResponse wishlistResponse;

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
                .sellingPrice(new BigDecimal("5.99"))
                .quantity(100)
                .status(ProductStatus.ACTIVE)
                .stockStatus(StockStatus.IN_STOCK)
                .active(true)
                .build();
        product.setId(200L);

        wishlist = Wishlist.builder()
                .user(user)
                .items(new ArrayList<>())
                .build();
        wishlist.setId(50L);

        wishlistItem = WishlistItem.builder()
                .id(10L)
                .wishlist(wishlist)
                .product(product)
                .build();

        wishlistResponse = WishlistResponse.builder()
                .id(50L)
                .userId(1L)
                .totalItems(1)
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
    @DisplayName("addProductToWishlist should validate, add item and save wishlist")
    void addProductToWishlist_Success() {
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findByIdAndDeletedFalse(200L)).thenReturn(Optional.of(product));
        doNothing().when(wishlistValidator).validateProductForWishlist(1L, product);
        when(wishlistRepository.findByUserId(1L)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);
        when(wishlistMapper.toResponse(any(Wishlist.class))).thenReturn(wishlistResponse);

        WishlistResponse response = wishlistService.addProductToWishlist(200L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(50L);

        verify(wishlistValidator).validateProductForWishlist(1L, product);
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("addProductToWishlist should throw ConflictException if duplicate")
    void addProductToWishlist_Duplicate_ThrowsConflict() {
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findByIdAndDeletedFalse(200L)).thenReturn(Optional.of(product));
        doThrow(new ConflictException("Product already in wishlist"))
                .when(wishlistValidator).validateProductForWishlist(1L, product);

        assertThatThrownBy(() -> wishlistService.addProductToWishlist(200L))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Product already in wishlist");
    }

    @Test
    @DisplayName("moveToCart should add product to cart and remove from wishlist")
    void moveToCart_Success() {
        wishlist.addItem(wishlistItem);

        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(cartService.addToCart(any())).thenReturn(CartResponse.builder().build());
        when(wishlistRepository.findByUserId(1L)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        CartResponse cartResponse = wishlistService.moveToCart(200L, "session-1");

        assertThat(cartResponse).isNotNull();
        assertThat(wishlist.getItems()).isEmpty();
        verify(cartService).addToCart(any());
    }

    @Test
    @DisplayName("getWishlistCount should return item count")
    void getWishlistCount_Success() {
        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(wishlistItemRepository.countByWishlistUserId(1L)).thenReturn(5L);

        WishlistCountResponse countResp = wishlistService.getWishlistCount();

        assertThat(countResp).isNotNull();
        assertThat(countResp.getCount()).isEqualTo(5L);
    }
}
