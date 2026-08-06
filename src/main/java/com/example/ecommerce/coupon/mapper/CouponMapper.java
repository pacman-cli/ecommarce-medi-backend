package com.example.ecommerce.coupon.mapper;

import com.example.ecommerce.coupon.dto.request.CouponRequest;
import com.example.ecommerce.coupon.dto.response.CouponResponse;
import com.example.ecommerce.coupon.entity.Coupon;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for converting {@link Coupon} entities to DTOs and vice versa.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface CouponMapper {

    CouponResponse toResponse(Coupon coupon);

    List<CouponResponse> toResponseList(List<Coupon> coupons);

    Coupon toEntity(CouponRequest request);

    void updateEntityFromRequest(CouponRequest request, @MappingTarget Coupon coupon);
}
