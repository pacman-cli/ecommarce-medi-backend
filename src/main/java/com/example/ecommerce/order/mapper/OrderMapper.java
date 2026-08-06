package com.example.ecommerce.order.mapper;

import com.example.ecommerce.order.dto.request.OrderAddressDto;
import com.example.ecommerce.order.dto.response.InvoiceResponse;
import com.example.ecommerce.order.dto.response.OrderAddressResponse;
import com.example.ecommerce.order.dto.response.OrderItemResponse;
import com.example.ecommerce.order.dto.response.OrderResponse;
import com.example.ecommerce.order.dto.response.OrderTimelineResponse;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.entity.OrderAddress;
import com.example.ecommerce.order.entity.OrderItem;
import com.example.ecommerce.order.entity.OrderTimeline;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for converting {@link Order} entities and line items to DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "customerEmail")
    OrderResponse toResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    OrderItemResponse toItemResponse(OrderItem item);

    OrderTimelineResponse toTimelineResponse(OrderTimeline timeline);

    OrderAddressResponse toAddressResponse(OrderAddress address);

    OrderAddress toAddressEntity(OrderAddressDto dto);

    @Mapping(source = "createdAt", target = "issueDate")
    @Mapping(source = "user.email", target = "customerEmail")
    @Mapping(expression = "java(order.getUser() != null ? order.getUser().getFirstName() + \" \" + order.getUser().getLastName() : order.getShippingAddress().getRecipientName())", target = "customerName")
    @Mapping(source = "status", target = "orderStatus")
    InvoiceResponse toInvoiceResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}
