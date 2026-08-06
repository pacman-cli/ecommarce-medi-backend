package com.example.ecommerce.delivery.mapper;

import com.example.ecommerce.delivery.dto.request.DeliveryPartnerRequest;
import com.example.ecommerce.delivery.dto.request.DeliveryZoneRequest;
import com.example.ecommerce.delivery.dto.response.DeliveryPartnerResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryShipmentResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryTimelineResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryTrackingResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryZoneResponse;
import com.example.ecommerce.delivery.entity.DeliveryPartner;
import com.example.ecommerce.delivery.entity.DeliveryShipment;
import com.example.ecommerce.delivery.entity.DeliveryTimeline;
import com.example.ecommerce.delivery.entity.DeliveryZone;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for converting between delivery entities and response DTOs.
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface DeliveryMapper {

    DeliveryPartnerResponse toPartnerResponse(DeliveryPartner partner);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DeliveryPartner toPartnerEntity(DeliveryPartnerRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartnerFromRequest(DeliveryPartnerRequest request, @MappingTarget DeliveryPartner partner);

    DeliveryZoneResponse toZoneResponse(DeliveryZone zone);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DeliveryZone toZoneEntity(DeliveryZoneRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateZoneFromRequest(DeliveryZoneRequest request, @MappingTarget DeliveryZone zone);

    DeliveryTimelineResponse toTimelineResponse(DeliveryTimeline timeline);

    DeliveryShipmentResponse toShipmentResponse(DeliveryShipment shipment);

    @Mapping(source = "status", target = "currentStatus")
    @Mapping(source = "partner.name", target = "partnerName")
    @Mapping(source = "timelines", target = "timelineHistory")
    DeliveryTrackingResponse toTrackingResponse(DeliveryShipment shipment);

    default List<DeliveryPartnerResponse> toPartnerResponseList(List<DeliveryPartner> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().filter(p -> !p.isDeleted()).map(this::toPartnerResponse).collect(Collectors.toList());
    }

    default List<DeliveryZoneResponse> toZoneResponseList(List<DeliveryZone> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().filter(z -> !z.isDeleted()).map(this::toZoneResponse).collect(Collectors.toList());
    }

    default List<DeliveryShipmentResponse> toShipmentResponseList(List<DeliveryShipment> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().filter(s -> !s.isDeleted()).map(this::toShipmentResponse).collect(Collectors.toList());
    }
}
