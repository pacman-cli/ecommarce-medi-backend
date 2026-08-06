package com.example.ecommerce.coupon.entity;

import com.example.ecommerce.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Audit log recording every individual user redemption of a coupon.
 */
@Entity
@Table(name = "coupon_usages")
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "used_at", nullable = false)
    private Instant usedAt = Instant.now();

    @Column(name = "order_id")
    private Long orderId;

    public CouponUsage() {
    }

    public CouponUsage(Long id, Coupon coupon, User user, Instant usedAt, Long orderId) {
        this.id = id;
        this.coupon = coupon;
        this.user = user;
        this.usedAt = usedAt != null ? usedAt : Instant.now();
        this.orderId = orderId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Instant getUsedAt() { return usedAt; }
    public void setUsedAt(Instant usedAt) { this.usedAt = usedAt; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public static CouponUsageBuilder builder() { return new CouponUsageBuilder(); }

    public static class CouponUsageBuilder {
        private Long id;
        private Coupon coupon;
        private User user;
        private Instant usedAt = Instant.now();
        private Long orderId;

        CouponUsageBuilder() {}

        public CouponUsageBuilder id(Long id) { this.id = id; return this; }
        public CouponUsageBuilder coupon(Coupon coupon) { this.coupon = coupon; return this; }
        public CouponUsageBuilder user(User user) { this.user = user; return this; }
        public CouponUsageBuilder usedAt(Instant usedAt) { this.usedAt = usedAt; return this; }
        public CouponUsageBuilder orderId(Long orderId) { this.orderId = orderId; return this; }

        public CouponUsage build() {
            return new CouponUsage(id, coupon, user, usedAt, orderId);
        }
    }
}
