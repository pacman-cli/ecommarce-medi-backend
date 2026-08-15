package com.example.ecommerce.review.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.exception.UnauthorizedException;
import com.example.ecommerce.order.entity.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.review.dto.request.CreateReviewRequest;
import com.example.ecommerce.review.dto.request.ReplyReviewRequest;
import com.example.ecommerce.review.dto.request.ReportReviewRequest;
import com.example.ecommerce.review.dto.request.ReviewFilterRequest;
import com.example.ecommerce.review.dto.request.UpdateReviewStatusRequest;
import com.example.ecommerce.review.dto.response.ReviewResponse;
import com.example.ecommerce.review.dto.response.ReviewSummaryResponse;
import com.example.ecommerce.review.entity.Review;
import com.example.ecommerce.review.entity.ReviewStatus;
import com.example.ecommerce.review.mapper.ReviewMapper;
import com.example.ecommerce.review.repository.ReviewRepository;
import com.example.ecommerce.review.service.ReviewService;
import com.example.ecommerce.review.specification.ReviewSpecification;
import com.example.ecommerce.review.validator.ReviewValidator;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import com.example.ecommerce.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Service implementation managing customer review submissions, verified purchase checks,
 * star rating statistics summaries, helpful votes, merchant replies and moderation workflows.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewValidator reviewValidator;

    @Override
    @Transactional
    public ReviewResponse createReview(CreateReviewRequest request) {
        User currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            throw new UnauthorizedException("User must be authenticated to submit a review");
        }

        reviewValidator.validateRating(request.getRating());
        reviewValidator.validateNewReview(request.getProductId(), currentUser.getId());

        Product product = productRepository.findByIdAndDeletedFalse(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));

        boolean isVerified = checkVerifiedPurchase(currentUser.getId(), request.getProductId());

        Review review = Review.builder()
                .product(product)
                .user(currentUser)
                .rating(request.getRating())
                .title(request.getTitle())
                .comment(request.getComment())
                .verifiedPurchase(isVerified)
                .status(ReviewStatus.APPROVED)
                .build();

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            int order = 0;
            for (String url : request.getImageUrls()) {
                review.addImage(url, order++);
            }
        }

        Review saved = reviewRepository.save(review);
        log.info("Successfully created review ID {} for product ID {}", saved.getId(), product.getId());
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReviewResponse voteHelpful(Long id) {
        Review review = reviewRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        review.incrementHelpful();
        Review saved = reviewRepository.save(review);
        log.info("Voted helpful for review ID {}, new count: {}", id, saved.getHelpfulCount());
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReviewResponse reportReview(Long id, ReportReviewRequest request) {
        Review review = reviewRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        review.incrementReported(request.getReason());
        Review saved = reviewRepository.save(review);
        log.warn("Review ID {} reported for reason: {}", id, request.getReason());
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReviewResponse replyToReview(Long id, ReplyReviewRequest request) {
        Review review = reviewRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        String actor = getCurrentUserEmail();
        review.setAdminReply(request.getReplyText());
        review.setRepliedAt(Instant.now());
        review.setRepliedBy(actor);

        Review saved = reviewRepository.save(review);
        log.info("Added merchant reply to review ID {} by {}", id, actor);
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReviewResponse updateReviewStatus(Long id, UpdateReviewStatusRequest request) {
        Review review = reviewRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        review.setStatus(request.getStatus());
        Review saved = reviewRepository.save(review);
        log.info("Updated review ID {} status to {}", id, request.getStatus());
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        review.setDeleted(true);
        review.setDeletedAt(Instant.now());
        reviewRepository.save(review);
        log.info("Soft deleted review ID {}", id);
    }

    @Override
    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        return reviewMapper.toResponse(review);
    }

    @Override
    public ReviewSummaryResponse getProductReviewSummary(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with ID: " + productId);
        }

        Double avgRating = reviewRepository.getAverageRatingByProductId(productId, ReviewStatus.APPROVED);
        long totalCount = reviewRepository.countByProductIdAndStatusAndDeletedFalse(productId, ReviewStatus.APPROVED);

        long star1 = reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(productId, 1, ReviewStatus.APPROVED);
        long star2 = reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(productId, 2, ReviewStatus.APPROVED);
        long star3 = reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(productId, 3, ReviewStatus.APPROVED);
        long star4 = reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(productId, 4, ReviewStatus.APPROVED);
        long star5 = reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(productId, 5, ReviewStatus.APPROVED);

        double roundedAvg = avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0;

        return ReviewSummaryResponse.builder()
                .productId(productId)
                .averageRating(roundedAvg)
                .totalReviews(totalCount)
                .star1Count(star1)
                .star2Count(star2)
                .star3Count(star3)
                .star4Count(star4)
                .star5Count(star5)
                .build();
    }

    @Override
    public PageResponse<ReviewResponse> getProductReviews(Long productId, ReviewFilterRequest filter, Pageable pageable) {
        if (filter == null) {
            filter = ReviewFilterRequest.builder().productId(productId).status(ReviewStatus.APPROVED).build();
        } else {
            filter.setProductId(productId);
            if (filter.getStatus() == null) {
                filter.setStatus(ReviewStatus.APPROVED);
            }
        }
        Specification<Review> spec = ReviewSpecification.build(filter);
        Page<Review> page = reviewRepository.findAll(spec, pageable);
        return PageResponse.from(page, reviewMapper::toResponse);
    }

    @Override
    public PageResponse<ReviewResponse> getAllReviews(ReviewFilterRequest filter, Pageable pageable) {
        Specification<Review> spec = ReviewSpecification.build(filter);
        Page<Review> page = reviewRepository.findAll(spec, pageable);
        return PageResponse.from(page, reviewMapper::toResponse);
    }

    private boolean checkVerifiedPurchase(Long userId, Long productId) {
        try {
            return orderRepository.existsByUserIdAndItemsProductIdAndStatus(userId, productId, OrderStatus.DELIVERED);
        } catch (Exception e) {
            log.warn("Error checking verified purchase for user {} product {}: {}", userId, productId, e.getMessage());
            return false;
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
        return user != null ? user.getEmail() : "Merchant Admin";
    }
}
