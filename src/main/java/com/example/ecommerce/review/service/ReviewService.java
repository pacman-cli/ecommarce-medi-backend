package com.example.ecommerce.review.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.review.dto.request.CreateReviewRequest;
import com.example.ecommerce.review.dto.request.ReplyReviewRequest;
import com.example.ecommerce.review.dto.request.ReportReviewRequest;
import com.example.ecommerce.review.dto.request.ReviewFilterRequest;
import com.example.ecommerce.review.dto.request.UpdateReviewStatusRequest;
import com.example.ecommerce.review.dto.response.ReviewResponse;
import com.example.ecommerce.review.dto.response.ReviewSummaryResponse;
import org.springframework.data.domain.Pageable;

/**
 * Service interface managing customer product reviews, ratings summary statistics,
 * verified purchase tags, helpful votes, merchant replies and moderation.
 */
public interface ReviewService {

    ReviewResponse createReview(CreateReviewRequest request);

    ReviewResponse voteHelpful(Long id);

    ReviewResponse reportReview(Long id, ReportReviewRequest request);

    ReviewResponse replyToReview(Long id, ReplyReviewRequest request);

    ReviewResponse updateReviewStatus(Long id, UpdateReviewStatusRequest request);

    void deleteReview(Long id);

    ReviewResponse getReviewById(Long id);

    ReviewSummaryResponse getProductReviewSummary(Long productId);

    PageResponse<ReviewResponse> getProductReviews(Long productId, ReviewFilterRequest filter, Pageable pageable);

    PageResponse<ReviewResponse> getAllReviews(ReviewFilterRequest filter, Pageable pageable);
}
