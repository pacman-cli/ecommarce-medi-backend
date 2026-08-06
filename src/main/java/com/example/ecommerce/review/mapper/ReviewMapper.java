package com.example.ecommerce.review.mapper;

import com.example.ecommerce.review.dto.response.ReviewImageResponse;
import com.example.ecommerce.review.dto.response.ReviewResponse;
import com.example.ecommerce.review.entity.Review;
import com.example.ecommerce.review.entity.ReviewImage;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper converting {@link Review} entities to response DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ReviewMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(expression = "java(review.getUser() != null ? review.getUser().getFirstName() + \" \" + review.getUser().getLastName() : \"Anonymous\")", target = "userName")
    @Mapping(source = "user.profileImageKey", target = "userProfileImage")
    ReviewResponse toResponse(Review review);

    ReviewImageResponse toImageResponse(ReviewImage image);

    List<ReviewResponse> toResponseList(List<Review> reviews);
}
