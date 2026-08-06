package com.example.ecommerce.review.service;

import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.review.dto.request.CreateReviewRequest;
import com.example.ecommerce.review.dto.request.ReplyReviewRequest;
import com.example.ecommerce.review.dto.request.ReportReviewRequest;
import com.example.ecommerce.review.dto.response.ReviewResponse;
import com.example.ecommerce.review.dto.response.ReviewSummaryResponse;
import com.example.ecommerce.review.entity.Review;
import com.example.ecommerce.review.entity.ReviewStatus;
import com.example.ecommerce.review.mapper.ReviewMapper;
import com.example.ecommerce.review.repository.ReviewRepository;
import com.example.ecommerce.review.service.impl.ReviewServiceImpl;
import com.example.ecommerce.review.validator.ReviewValidator;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

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

    private User user;
    private Product product;
    private Review review;
    private ReviewResponse reviewResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.CUSTOMER)
                .build();
        user.setId(1L);

        product = Product.builder()
                .name("Paracetamol 500mg")
                .sku("MED-PARA-500")
                .build();
        product.setId(200L);

        review = Review.builder()
                .product(product)
                .user(user)
                .rating(5)
                .title("Great Medicine")
                .comment("Effective and quick delivery.")
                .verifiedPurchase(true)
                .status(ReviewStatus.APPROVED)
                .build();
        review.setId(50L);

        reviewResponse = ReviewResponse.builder()
                .id(50L)
                .productId(200L)
                .productName("Paracetamol 500mg")
                .userId(1L)
                .userName("John Doe")
                .rating(5)
                .title("Great Medicine")
                .comment("Effective and quick delivery.")
                .verifiedPurchase(true)
                .status(ReviewStatus.APPROVED)
                .build();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "john.doe@example.com", "password", Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("createReview should validate, attach photo images, save review and return DTO")
    void createReview_Success() {
        CreateReviewRequest request = CreateReviewRequest.builder()
                .productId(200L)
                .rating(5)
                .title("Great Medicine")
                .comment("Effective and quick delivery.")
                .imageUrls(List.of("https://images.example.com/photo1.jpg"))
                .build();

        when(userRepository.findByEmailIgnoreCase("john.doe@example.com")).thenReturn(Optional.of(user));
        doNothing().when(reviewValidator).validateRating(5);
        doNothing().when(reviewValidator).validateNewReview(200L, 1L);
        when(productRepository.findByIdAndDeletedFalse(200L)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toResponse(any(Review.class))).thenReturn(reviewResponse);

        ReviewResponse response = reviewService.createReview(request);

        assertThat(response).isNotNull();
        assertThat(response.getRating()).isEqualTo(5);

        verify(reviewValidator).validateRating(5);
        verify(reviewValidator).validateNewReview(200L, 1L);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("voteHelpful should increment helpful count and save")
    void voteHelpful_Success() {
        when(reviewRepository.findByIdAndDeletedFalse(50L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toResponse(any(Review.class))).thenReturn(reviewResponse);

        ReviewResponse response = reviewService.voteHelpful(50L);

        assertThat(response).isNotNull();
        assertThat(review.getHelpfulCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("reportReview should flag review and record reason")
    void reportReview_Success() {
        ReportReviewRequest reportReq = ReportReviewRequest.builder()
                .reason("Inappropriate language")
                .build();

        when(reviewRepository.findByIdAndDeletedFalse(50L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toResponse(any(Review.class))).thenReturn(reviewResponse);

        ReviewResponse response = reviewService.reportReview(50L, reportReq);

        assertThat(response).isNotNull();
        assertThat(review.isReported()).isTrue();
        assertThat(review.getReportReason()).isEqualTo("Inappropriate language");
    }

    @Test
    @DisplayName("replyToReview should add merchant reply and timestamp")
    void replyToReview_Success() {
        ReplyReviewRequest replyReq = ReplyReviewRequest.builder()
                .replyText("Thank you for your feedback!")
                .build();

        when(reviewRepository.findByIdAndDeletedFalse(50L)).thenReturn(Optional.of(review));
        when(userRepository.findByEmailIgnoreCase("john.doe@example.com")).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toResponse(any(Review.class))).thenReturn(reviewResponse);

        ReviewResponse response = reviewService.replyToReview(50L, replyReq);

        assertThat(response).isNotNull();
        assertThat(review.getAdminReply()).isEqualTo("Thank you for your feedback!");
    }

    @Test
    @DisplayName("getProductReviewSummary should return rounded average rating and star distribution")
    void getProductReviewSummary_Success() {
        when(productRepository.existsById(200L)).thenReturn(true);
        when(reviewRepository.getAverageRatingByProductId(200L, ReviewStatus.APPROVED)).thenReturn(4.567);
        when(reviewRepository.countByProductIdAndStatusAndDeletedFalse(200L, ReviewStatus.APPROVED)).thenReturn(10L);
        when(reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(200L, 1, ReviewStatus.APPROVED)).thenReturn(0L);
        when(reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(200L, 2, ReviewStatus.APPROVED)).thenReturn(0L);
        when(reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(200L, 3, ReviewStatus.APPROVED)).thenReturn(0L);
        when(reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(200L, 4, ReviewStatus.APPROVED)).thenReturn(4L);
        when(reviewRepository.countByProductIdAndRatingAndStatusAndDeletedFalse(200L, 5, ReviewStatus.APPROVED)).thenReturn(6L);

        ReviewSummaryResponse summary = reviewService.getProductReviewSummary(200L);

        assertThat(summary).isNotNull();
        assertThat(summary.getAverageRating()).isEqualTo(4.6);
        assertThat(summary.getTotalReviews()).isEqualTo(10L);
        assertThat(summary.getStar5Count()).isEqualTo(6L);
    }
}
