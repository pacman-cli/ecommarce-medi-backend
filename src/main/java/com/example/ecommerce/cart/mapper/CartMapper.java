package com.example.ecommerce.cart.mapper;

import com.example.ecommerce.cart.dto.response.CartItemResponse;
import com.example.ecommerce.cart.dto.response.CartResponse;
import com.example.ecommerce.cart.entity.Cart;
import com.example.ecommerce.cart.entity.CartItem;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for converting {@link Cart} and {@link CartItem} entities to response DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface CartMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "items", target = "items")
    @Mapping(target = "totalItems", expression = "java(cart.getItems() != null ? cart.getItems().stream().mapToInt(com.example.ecommerce.cart.entity.CartItem::getQuantity).sum() : 0)")
    CartResponse toResponse(Cart cart);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(source = "product.slug", target = "productSlug")
    @Mapping(source = "product.thumbnail", target = "thumbnail")
    @Mapping(target = "inStock", expression = "java(item.getProduct() != null && item.getProduct().getQuantity() != null && item.getProduct().getQuantity() >= item.getQuantity())")
    CartItemResponse toItemResponse(CartItem item);

    List<CartItemResponse> toItemResponseList(List<CartItem> items);
}
