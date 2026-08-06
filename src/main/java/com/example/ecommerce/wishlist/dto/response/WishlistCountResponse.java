package com.example.ecommerce.wishlist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Summary DTO reporting total item count in user's wishlist.
 */
@Schema(description = "Wishlist item count response")
public class WishlistCountResponse {

    @Schema(description = "Total item count", example = "5")
    private long count;

    public WishlistCountResponse() {
    }

    public WishlistCountResponse(long count) {
        this.count = count;
    }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
}
