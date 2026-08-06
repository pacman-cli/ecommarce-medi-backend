package com.example.ecommerce.wishlist.entity;

import com.example.ecommerce.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

/**
 * Line item entry inside a user's wishlist.
 */
@Entity
@Table(
        name = "wishlist_items",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_wishlist_items_wishlist_product", columnNames = {"wishlist_id", "product_id"})
        }
)
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "added_at", nullable = false)
    private Instant addedAt = Instant.now();

    public WishlistItem() {
    }

    public WishlistItem(Long id, Wishlist wishlist, Product product, Instant addedAt) {
        this.id = id;
        this.wishlist = wishlist;
        this.product = product;
        this.addedAt = addedAt != null ? addedAt : Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Wishlist getWishlist() { return wishlist; }
    public void setWishlist(Wishlist wishlist) { this.wishlist = wishlist; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Instant getAddedAt() { return addedAt; }
    public void setAddedAt(Instant addedAt) { this.addedAt = addedAt; }

    public static WishlistItemBuilder builder() { return new WishlistItemBuilder(); }

    public static class WishlistItemBuilder {
        private Long id;
        private Wishlist wishlist;
        private Product product;
        private Instant addedAt = Instant.now();

        WishlistItemBuilder() {}

        public WishlistItemBuilder id(Long id) { this.id = id; return this; }
        public WishlistItemBuilder wishlist(Wishlist wishlist) { this.wishlist = wishlist; return this; }
        public WishlistItemBuilder product(Product product) { this.product = product; return this; }
        public WishlistItemBuilder addedAt(Instant addedAt) { this.addedAt = addedAt; return this; }

        public WishlistItem build() {
            return new WishlistItem(id, wishlist, product, addedAt);
        }
    }
}
