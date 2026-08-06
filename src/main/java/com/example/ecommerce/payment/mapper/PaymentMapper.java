package com.example.ecommerce.payment.mapper;

import com.example.ecommerce.payment.dto.response.PaymentResponse;
import com.example.ecommerce.payment.entity.Payment;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper converting {@link Payment} entities to DTO projections.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PaymentMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.orderNumber", target = "orderNumber")
    PaymentResponse toResponse(Payment payment);

    List<PaymentResponse> toResponseList(List<Payment> payments);
}
