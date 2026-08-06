package com.example.ecommerce.review.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Enterprise product review aggregate root entity supporting 1-5 star ratings, verified purchase tags,
 * photo attachments, helpful votes, admin responses and content moderation flags.
 */
@Entity
@Table(name = "reviews")
@SQLDelete(sql = "UPDATE reviews SET deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 150)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "verified_purchase", nullable = false)
    private boolean verifiedPurchase = false;

    @Column(name = "helpful_count", nullable = false)
    private Integer helpfulCount = 0;

    @Column(name = "reported_count", nullable = false)
    private Integer reportedCount = 0;

    @Column(name = "is_reported", nullable = false)
    private boolean isReported = false;

    @Column(name = "report_reason", length = 250)
    private String reportReason;

    @Lob
    @Column(name = "admin_reply", columnDefinition = "TEXT")
    private String adminReply;

    @Column(name = "replied_at")
    private Instant repliedAt;

    @Column(name = "replied_by", length = 100)
    private String repliedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReviewStatus status = ReviewStatus.APPROVED;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Review() {
    }

    public void addImage(String imageUrl, int sortOrder) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        ReviewImage img = ReviewImage.builder()
                .review(this)
                .imageUrl(imageUrl)
                .sortOrder(sortOrder)
                .build();
        this.images.add(img);
    }

    public void incrementHelpful() {
        this.helpfulCount = (this.helpfulCount != null ? this.helpfulCount : 0) + 1;
    }

    public void incrementReported(String reason) {
        this.reportedCount = (this.reportedCount != null ? this.reportedCount : 0) + 1;
        this.isReported = true;
        this.reportReason = reason;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public List<ReviewImage> getImages() { return images; }
    public void setImages(List<ReviewImage> images) { this.images = images; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public static ReviewBuilder builder() { return new ReviewBuilder(); }

    public static class ReviewBuilder {
        private Product product;
        private User user;
        private Integer rating;
        private String title;
        private String comment;
        private boolean verifiedPurchase = false;
        private Integer helpfulCount = 0;
        private Integer reportedCount = 0;
        private boolean isReported = false;
        private String reportReason;
        private String adminReply;
        private Instant repliedAt;
        private String repliedBy;
        private ReviewStatus status = ReviewStatus.APPROVED;
        private List<ReviewImage> images = new ArrayList<>();
        private boolean deleted = false;
        private Instant deletedAt;

        ReviewBuilder() {}

        public ReviewBuilder product(Product product) { this.product = product; return this; }
        public ReviewBuilder user(User user) { this.user = user; return this; }
        public ReviewBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewBuilder title(String title) { this.title = title; return this; }
        public ReviewBuilder comment(String comment) { this.comment = comment; return this; }
        public ReviewBuilder verifiedPurchase(boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; return this; }
        public ReviewBuilder helpfulCount(Integer helpfulCount) { this.helpfulCount = helpfulCount; return this; }
        public ReviewBuilder reportedCount(Integer reportedCount) { this.reportedCount = reportedCount; return this; }
        public ReviewBuilder isReported(boolean isReported) { this.isReported = isReported; return this; }
        public ReviewBuilder reportReason(String reportReason) { this.reportReason = reportReason; return this; }
        public ReviewBuilder adminReply(String adminReply) { this.adminReply = adminReply; return this; }
        public ReviewBuilder repliedAt(Instant repliedAt) { this.repliedAt = repliedAt; return this; }
        public ReviewBuilder repliedBy(String repliedBy) { this.repliedBy = repliedBy; return this; }
        public ReviewBuilder status(ReviewStatus status) { this.status = status; return this; }
        public ReviewBuilder images(List<ReviewImage> images) { this.images = images; return this; }
        public ReviewBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public ReviewBuilder deletedAt(Instant deletedAt) { this.deletedAt = deletedAt; return this; }

        public Review build() {
            Review r = new Review();
            r.setProduct(product);
            r.setUser(user);
            r.setRating(rating);
            r.setTitle(title);
            r.setComment(comment);
            r.setVerifiedPurchase(verifiedPurchase);
            r.setHelpfulCount(helpfulCount != null ? helpfulCount : 0);
            r.setReportedCount(reportedCount != null ? reportedCount : 0);
            r.setReported(isReported);
            r.setReportReason(reportReason);
            r.setAdminReply(adminReply);
            r.setRepliedAt(repliedAt);
            r.setRepliedBy(repliedBy);
            r.setStatus(status != null ? status : ReviewStatus.APPROVED);
            r.setImages(images != null ? images : new ArrayList<>());
            r.setDeleted(deleted);
            r.setDeletedAt(deletedAt);
            return r;
        }
    }
}
