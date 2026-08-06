package com.example.ecommerce.review.dto.response;

import com.example.ecommerce.review.entity.ReviewStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Detailed master review response DTO.
 */
@Schema(description = "Customer product review details response")
public class ReviewResponse {

    @Schema(description = "Review ID", example = "50")
    private Long id;

    @Schema(description = "Product ID", example = "200")
    private Long productId;

    @Schema(description = "Product name", example = "Paracetamol 500mg Tablets")
    private String productName;

    @Schema(description = "Author user ID", example = "1")
    private Long userId;

    @Schema(description = "Author full name", example = "John Doe")
    private String userName;

    @Schema(description = "Author profile image URL", example = "https://images.example.com/profiles/john.jpg")
    private String userProfileImage;

    @Schema(description = "Rating (1-5 stars)", example = "5")
    private Integer rating;

    @Schema(description = "Headline title", example = "Excellent quality product!")
    private String title;

    @Schema(description = "Detailed comment", example = "Fast delivery and great quality medicine.")
    private String comment;

    @Schema(description = "Verified purchaser tag flag", example = "true")
    private boolean verifiedPurchase;

    @Schema(description = "Number of helpful votes", example = "12")
    private Integer helpfulCount;

    @Schema(description = "Report count if flagged", example = "0")
    private Integer reportedCount;

    @Schema(description = "Reported flag", example = "false")
    private boolean isReported;

    @Schema(description = "Report reason description")
    private String reportReason;

    @Schema(description = "Merchant or admin reply response")
    private String adminReply;

    @Schema(description = "Timestamp of merchant reply")
    private Instant repliedAt;

    @Schema(description = "Author of merchant reply", example = "admin@example.com")
    private String repliedBy;

    @Schema(description = "Moderation status", example = "APPROVED")
    private ReviewStatus status;

    @Schema(description = "Attached review photo images")
    private List<ReviewImageResponse> images;

    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    public ReviewResponse() {
    }

    public ReviewResponse(Long id, Long productId, String productName, Long userId, String userName, String userProfileImage, Integer rating, String title, String comment, boolean verifiedPurchase, Integer helpfulCount, Integer reportedCount, boolean isReported, String reportReason, String adminReply, Instant repliedAt, String repliedBy, ReviewStatus status, List<ReviewImageResponse> images, Instant createdAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.userId = userId;
        this.userName = userName;
        this.userProfileImage = userProfileImage;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.verifiedPurchase = verifiedPurchase;
        this.helpfulCount = helpfulCount;
        this.reportedCount = reportedCount;
        this.isReported = isReported;
        this.reportReason = reportReason;
        this.adminReply = adminReply;
        this.repliedAt = repliedAt;
        this.repliedBy = repliedBy;
        this.status = status;
        this.images = images;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserProfileImage() { return userProfileImage; }
    public void setUserProfileImage(String userProfileImage) { this.userProfileImage = userProfileImage; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public boolean isVerifiedPurchase() { return verifiedPurchase; }
    public void setVerifiedPurchase(boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; }

    public Integer getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(Integer helpfulCount) { this.helpfulCount = helpfulCount; }

    public Integer getReportedCount() { return reportedCount; }
    public void setReportedCount(Integer reportedCount) { this.reportedCount = reportedCount; }

    public boolean isReported() { return isReported; }
    public void setReported(boolean reported) { isReported = reported; }

    public String getReportReason() { return reportReason; }
    public void setReportReason(String reportReason) { this.reportReason = reportReason; }

    public String getAdminReply() { return adminReply; }
    public void setAdminReply(String adminReply) { this.adminReply = adminReply; }

    public Instant getRepliedAt() { return repliedAt; }
    public void setRepliedAt(Instant repliedAt) { this.repliedAt = repliedAt; }

    public String getRepliedBy() { return repliedBy; }
    public void setRepliedBy(String repliedBy) { this.repliedBy = repliedBy; }

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    public List<ReviewImageResponse> getImages() { return images; }
    public void setImages(List<ReviewImageResponse> images) { this.images = images; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static ReviewResponseBuilder builder() { return new ReviewResponseBuilder(); }

    public static class ReviewResponseBuilder {
        private Long id;
        private Long productId;
        private String productName;
        private Long userId;
        private String userName;
        private String userProfileImage;
        private Integer rating;
        private String title;
        private String comment;
        private boolean verifiedPurchase;
        private Integer helpfulCount;
        private Integer reportedCount;
        private boolean isReported;
        private String reportReason;
        private String adminReply;
        private Instant repliedAt;
        private String repliedBy;
        private ReviewStatus status;
        private List<ReviewImageResponse> images;
        private Instant createdAt;

        ReviewResponseBuilder() {}

        public ReviewResponseBuilder id(Long id) { this.id = id; return this; }
        public ReviewResponseBuilder productId(Long productId) { this.productId = productId; return this; }
        public ReviewResponseBuilder productName(String productName) { this.productName = productName; return this; }
        public ReviewResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public ReviewResponseBuilder userName(String userName) { this.userName = userName; return this; }
        public ReviewResponseBuilder userProfileImage(String userProfileImage) { this.userProfileImage = userProfileImage; return this; }
        public ReviewResponseBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewResponseBuilder title(String title) { this.title = title; return this; }
        public ReviewResponseBuilder comment(String comment) { this.comment = comment; return this; }
        public ReviewResponseBuilder verifiedPurchase(boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; return this; }
        public ReviewResponseBuilder helpfulCount(Integer helpfulCount) { this.helpfulCount = helpfulCount; return this; }
        public ReviewResponseBuilder reportedCount(Integer reportedCount) { this.reportedCount = reportedCount; return this; }
        public ReviewResponseBuilder isReported(boolean isReported) { this.isReported = isReported; return this; }
        public ReviewResponseBuilder reportReason(String reportReason) { this.reportReason = reportReason; return this; }
        public ReviewResponseBuilder adminReply(String adminReply) { this.adminReply = adminReply; return this; }
        public ReviewResponseBuilder repliedAt(Instant repliedAt) { this.repliedAt = repliedAt; return this; }
        public ReviewResponseBuilder repliedBy(String repliedBy) { this.repliedBy = repliedBy; return this; }
        public ReviewResponseBuilder status(ReviewStatus status) { this.status = status; return this; }
        public ReviewResponseBuilder images(List<ReviewImageResponse> images) { this.images = images; return this; }
        public ReviewResponseBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public ReviewResponse build() {
            return new ReviewResponse(id, productId, productName, userId, userName, userProfileImage, rating, title, comment, verifiedPurchase, helpfulCount, reportedCount, isReported, reportReason, adminReply, repliedAt, repliedBy, status, images, createdAt);
        }
    }
}
