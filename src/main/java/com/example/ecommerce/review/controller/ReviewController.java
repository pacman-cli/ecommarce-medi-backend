package com.example.ecommerce.review.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.review.dto.request.CreateReviewRequest;
import com.example.ecommerce.review.dto.request.ReplyReviewRequest;
import com.example.ecommerce.review.dto.request.ReportReviewRequest;
import com.example.ecommerce.review.dto.request.ReviewFilterRequest;
import com.example.ecommerce.review.dto.request.UpdateReviewStatusRequest;
import com.example.ecommerce.review.dto.response.ReviewResponse;
import com.example.ecommerce.review.dto.response.ReviewSummaryResponse;
import com.example.ecommerce.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing endpoints for product customer reviews, rating statistics,
 * helpful votes, merchant replies and moderation workflows.
 */
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "Endpoints for product reviews, ratings summary statistics, verified purchases, helpful votes and merchant responses")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Submit product review", description = "Submits a 1-5 star review with headline, comment and optional photo attachments for a product")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Review submitted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Already reviewed or invalid rating value")
    })
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Review submitted successfully"));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get product reviews", description = "Retrieves paginated listing of approved reviews for a specific product")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product reviews retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getProductReviews(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId,
            @ModelAttribute ReviewFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ReviewResponse> page = reviewService.getProductReviews(productId, filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Product reviews retrieved successfully"));
    }

    @GetMapping("/product/{productId}/summary")
    @Operation(summary = "Get product rating summary", description = "Calculates average star rating, total review count and 1-5 star breakdown distribution for a product")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rating summary retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<ReviewSummaryResponse>> getProductReviewSummary(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId) {
        ReviewSummaryResponse summary = reviewService.getProductReviewSummary(productId);
        return ResponseEntity.ok(ApiResponse.success(summary, "Rating summary retrieved successfully"));
    }

    @PostMapping("/{id}/helpful")
    @Operation(summary = "Vote review as helpful", description = "Increments helpful vote counter for a review")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Helpful vote recorded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<ReviewResponse>> voteHelpful(
            @Parameter(description = "Review ID", required = true) @PathVariable Long id) {
        ReviewResponse response = reviewService.voteHelpful(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Helpful vote recorded successfully"));
    }

    @PostMapping("/{id}/report")
    @Operation(summary = "Report abusive review", description = "Flags a review for moderation due to inappropriate language or policy violations")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review report recorded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<ReviewResponse>> reportReview(
            @Parameter(description = "Review ID", required = true) @PathVariable Long id,
            @Valid @RequestBody ReportReviewRequest request) {
        ReviewResponse response = reviewService.reportReview(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Review report recorded successfully"));
    }

    @PostMapping("/{id}/reply")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add merchant reply", description = "Appends an official merchant or admin response to a customer review (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Merchant reply added successfully")
    })
    public ResponseEntity<ApiResponse<ReviewResponse>> replyToReview(
            @Parameter(description = "Review ID", required = true) @PathVariable Long id,
            @Valid @RequestBody ReplyReviewRequest request) {
        ReviewResponse response = reviewService.replyToReview(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Merchant reply added successfully"));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update review moderation status", description = "Approves or rejects customer review submission (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review moderation status updated successfully")
    })
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReviewStatus(
            @Parameter(description = "Review ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateReviewStatusRequest request) {
        ReviewResponse response = reviewService.updateReviewStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Review moderation status updated successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all reviews for moderation", description = "Retrieves paginated master listing of reviews with moderation status and report filters (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All reviews retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getAllReviews(
            @ModelAttribute ReviewFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ReviewResponse> page = reviewService.getAllReviews(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "All reviews retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete review", description = "Soft deletes a review by ID (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Review deleted successfully")
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID", required = true) @PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
