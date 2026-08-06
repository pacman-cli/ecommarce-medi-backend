package com.example.ecommerce.wishlist.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * User wishlist aggregate root entity.
 */
@Entity
@Table(name = "wishlists")
public class Wishlist extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("addedAt DESC")
    private List<WishlistItem> items = new ArrayList<>();

    public Wishlist() {
    }

    public Wishlist(User user, List<WishlistItem> items) {
        this.user = user;
        this.items = items != null ? items : new ArrayList<>();
    }

    public void addItem(WishlistItem item) {
        if (item != null) {
            if (this.items == null) {
                this.items = new ArrayList<>();
            }
            item.setWishlist(this);
            this.items.add(item);
        }
    }

    public void removeItem(WishlistItem item) {
        if (item != null && this.items != null) {
            this.items.remove(item);
            item.setWishlist(null);
        }
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<WishlistItem> getItems() { return items; }
    public void setItems(List<WishlistItem> items) { this.items = items; }

    public static WishlistBuilder builder() { return new WishlistBuilder(); }

    public static class WishlistBuilder {
        private User user;
        private List<WishlistItem> items = new ArrayList<>();

        WishlistBuilder() {}

        public WishlistBuilder user(User user) { this.user = user; return this; }
        public WishlistBuilder items(List<WishlistItem> items) { this.items = items; return this; }

        public Wishlist build() {
            return new Wishlist(user, items);
        }
    }
}
