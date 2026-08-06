package com.example.ecommerce.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Image attachment associated with a product customer review.
 */
@Entity
@Table(name = "review_images")
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    public ReviewImage() {
    }

    public ReviewImage(Long id, Review review, String imageUrl, Integer sortOrder) {
        this.id = id;
        this.review = review;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Review getReview() { return review; }
    public void setReview(Review review) { this.review = review; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public static ReviewImageBuilder builder() { return new ReviewImageBuilder(); }

    public static class ReviewImageBuilder {
        private Long id;
        private Review review;
        private String imageUrl;
        private Integer sortOrder = 0;

        ReviewImageBuilder() {}

        public ReviewImageBuilder id(Long id) { this.id = id; return this; }
        public ReviewImageBuilder review(Review review) { this.review = review; return this; }
        public ReviewImageBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ReviewImageBuilder sortOrder(Integer sortOrder) { this.sortOrder = sortOrder; return this; }

        public ReviewImage build() {
            return new ReviewImage(id, review, imageUrl, sortOrder);
        }
    }
}
