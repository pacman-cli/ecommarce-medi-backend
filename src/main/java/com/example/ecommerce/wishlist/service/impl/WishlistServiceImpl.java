package com.example.ecommerce.wishlist.service.impl;

import com.example.ecommerce.cart.dto.request.AddToCartRequest;
import com.example.ecommerce.cart.dto.response.CartResponse;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.UnauthorizedException;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import com.example.ecommerce.wishlist.dto.request.WishlistFilterRequest;
import com.example.ecommerce.wishlist.dto.response.WishlistCountResponse;
import com.example.ecommerce.wishlist.dto.response.WishlistItemResponse;
import com.example.ecommerce.wishlist.dto.response.WishlistResponse;
import com.example.ecommerce.wishlist.entity.Wishlist;
import com.example.ecommerce.wishlist.entity.WishlistItem;
import com.example.ecommerce.wishlist.mapper.WishlistMapper;
import com.example.ecommerce.wishlist.repository.WishlistItemRepository;
import com.example.ecommerce.wishlist.repository.WishlistRepository;
import com.example.ecommerce.wishlist.service.WishlistService;
import com.example.ecommerce.wishlist.specification.WishlistSpecification;
import com.example.ecommerce.wishlist.validator.WishlistValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation managing user wishlists, move-to-cart workflows,
 * recently added lookups and paginated search.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final WishlistMapper wishlistMapper;
    private final WishlistValidator wishlistValidator;

    @Override
    @Transactional
    public WishlistResponse getWishlist() {
        User user = getCurrentUser();
        Wishlist wishlist = getOrCreateWishlist(user);
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    @Transactional
    public WishlistResponse addProductToWishlist(Long productId) {
        User user = getCurrentUser();
        log.info("Adding product ID {} to wishlist for user ID {}", productId, user.getId());

        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        wishlistValidator.validateProductForWishlist(user.getId(), product);

        Wishlist wishlist = getOrCreateWishlist(user);

        WishlistItem item = WishlistItem.builder()
                .wishlist(wishlist)
                .product(product)
                .addedAt(Instant.now())
                .build();

        wishlist.addItem(item);
        Wishlist saved = wishlistRepository.save(wishlist);
        log.info("Successfully added product ID {} to wishlist ID {}", productId, saved.getId());
        return wishlistMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public WishlistResponse removeProductFromWishlist(Long productId) {
        User user = getCurrentUser();
        log.info("Removing product ID {} from wishlist for user ID {}", productId, user.getId());

        Wishlist wishlist = getOrCreateWishlist(user);
        Optional<WishlistItem> itemOpt = wishlist.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (itemOpt.isPresent()) {
            wishlist.removeItem(itemOpt.get());
            wishlistRepository.save(wishlist);
        }

        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    @Transactional
    public CartResponse moveToCart(Long productId, String sessionId) {
        User user = getCurrentUser();
        log.info("Moving product ID {} from wishlist to cart for user ID {}", productId, user.getId());

        // Add to cart with quantity 1
        AddToCartRequest addReq = AddToCartRequest.builder()
                .productId(productId)
                .quantity(1)
                .sessionId(sessionId)
                .build();
        CartResponse cartResponse = cartService.addToCart(addReq);

        // Remove from wishlist
        removeProductFromWishlist(productId);

        return cartResponse;
    }

    @Override
    public WishlistCountResponse getWishlistCount() {
        User user = getCurrentUser();
        long count = wishlistItemRepository.countByWishlistUserId(user.getId());
        return new WishlistCountResponse(count);
    }

    @Override
    public List<WishlistItemResponse> getRecentlyAdded(int limit) {
        User user = getCurrentUser();
        int validLimit = limit > 0 ? limit : 5;
        Pageable pageable = PageRequest.of(0, validLimit, Sort.by(Sort.Direction.DESC, "addedAt"));
        List<WishlistItem> list = wishlistItemRepository.findByWishlistUserIdOrderByAddedAtDesc(user.getId(), pageable);
        return wishlistMapper.toItemResponseList(list);
    }

    @Override
    public PageResponse<WishlistItemResponse> getWishlistItems(WishlistFilterRequest filter, Pageable pageable) {
        User user = getCurrentUser();
        Specification<WishlistItem> spec = WishlistSpecification.build(user.getId(), filter);
        Page<WishlistItem> page = wishlistItemRepository.findAll(spec, pageable);
        return PageResponse.from(page, wishlistMapper::toItemResponse);
    }

    private Wishlist getOrCreateWishlist(User user) {
        return wishlistRepository.findByUserId(user.getId())
                .orElseGet(() -> wishlistRepository.save(Wishlist.builder()
                        .user(user)
                        .build()));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new UnauthorizedException("User must be authenticated to access wishlist");
        }
        String email = auth.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("Authenticated user not found"));
    }
}
