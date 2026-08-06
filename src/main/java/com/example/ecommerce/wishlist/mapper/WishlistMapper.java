package com.example.ecommerce.wishlist.mapper;

import com.example.ecommerce.wishlist.dto.response.WishlistItemResponse;
import com.example.ecommerce.wishlist.dto.response.WishlistResponse;
import com.example.ecommerce.wishlist.entity.Wishlist;
import com.example.ecommerce.wishlist.entity.WishlistItem;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for converting {@link Wishlist} and {@link WishlistItem} entities to DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface WishlistMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "items", target = "items")
    @Mapping(target = "totalItems", expression = "java(wishlist.getItems() != null ? wishlist.getItems().size() : 0)")
    WishlistResponse toResponse(Wishlist wishlist);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(source = "product.slug", target = "productSlug")
    @Mapping(source = "product.thumbnail", target = "thumbnail")
    @Mapping(source = "product.sellingPrice", target = "sellingPrice")
    @Mapping(source = "product.discountPrice", target = "discountPrice")
    @Mapping(target = "inStock", expression = "java(item.getProduct() != null && item.getProduct().getQuantity() != null && item.getProduct().getQuantity() > 0)")
    WishlistItemResponse toItemResponse(WishlistItem item);

    List<WishlistItemResponse> toItemResponseList(List<WishlistItem> items);
}
