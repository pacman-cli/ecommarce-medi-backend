package com.example.ecommerce.review.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates review creation rules and rating star limits.
 */
@Component
@RequiredArgsConstructor
public class ReviewValidator {

    private final ReviewRepository reviewRepository;

    public void validateNewReview(Long productId, Long userId) {
        if (reviewRepository.existsByProductIdAndUserIdAndDeletedFalse(productId, userId)) {
            throw new BadRequestException("You have already submitted a review for this product");
        }
    }

    public void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5 stars");
        }
    }
}
