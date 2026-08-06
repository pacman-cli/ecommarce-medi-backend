package com.example.ecommerce.delivery.service.impl;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
import com.example.ecommerce.delivery.dto.enums.ShippingMethod;
import com.example.ecommerce.delivery.dto.request.AssignRiderRequest;
import com.example.ecommerce.delivery.dto.request.CreateShipmentRequest;
import com.example.ecommerce.delivery.dto.request.DeliveryChargeCalculateRequest;
import com.example.ecommerce.delivery.dto.request.DeliveryPartnerRequest;
import com.example.ecommerce.delivery.dto.request.DeliveryZoneRequest;
import com.example.ecommerce.delivery.dto.request.UpdateDeliveryStatusRequest;
import com.example.ecommerce.delivery.dto.response.DeliveryChargeResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryPartnerResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryShipmentResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryTrackingResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryZoneResponse;
import com.example.ecommerce.delivery.entity.DeliveryPartner;
import com.example.ecommerce.delivery.entity.DeliveryShipment;
import com.example.ecommerce.delivery.entity.DeliveryZone;
import com.example.ecommerce.delivery.mapper.DeliveryMapper;
import com.example.ecommerce.delivery.repository.DeliveryPartnerRepository;
import com.example.ecommerce.delivery.repository.DeliveryShipmentRepository;
import com.example.ecommerce.delivery.repository.DeliveryZoneRepository;
import com.example.ecommerce.delivery.service.DeliveryService;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation managing delivery rates, shipments, riders, partners, zones, and tracking timelines.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryPartnerRepository partnerRepository;
    private final DeliveryZoneRepository zoneRepository;
    private final DeliveryShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    @Transactional(readOnly = true)
    public DeliveryChargeResponse calculateDeliveryCharge(DeliveryChargeCalculateRequest request) {
        log.info("Calculating delivery charge for method: {}, division: {}, district: {}",
                request.getShippingMethod(), request.getDivision(), request.getDistrict());

        DeliveryZone zone = resolveZone(request.getZoneId(), request.getDivision(), request.getDistrict());

        BigDecimal baseCharge = zone != null ? zone.getBaseFee() : new BigDecimal("60.00");
        BigDecimal expressFee = zone != null ? zone.getExpressFee() : new BigDecimal("120.00");
        BigDecimal codFee = (Boolean.TRUE.equals(request.getIsCod()) && zone != null) ? zone.getCodFee() : BigDecimal.ZERO;

        BigDecimal expressSurcharge = BigDecimal.ZERO;
        if (request.getShippingMethod() == ShippingMethod.EXPRESS) {
            expressSurcharge = expressFee;
        } else if (request.getShippingMethod() == ShippingMethod.SAME_DAY) {
            expressSurcharge = expressFee.multiply(new BigDecimal("1.5"));
        }

        BigDecimal totalCharge = baseCharge.add(expressSurcharge).add(codFee);

        int minDays = zone != null ? zone.getMinDeliveryDays() : 1;
        int maxDays = zone != null ? zone.getMaxDeliveryDays() : 3;

        if (request.getShippingMethod() == ShippingMethod.SAME_DAY) {
            minDays = 0;
            maxDays = 0;
        } else if (request.getShippingMethod() == ShippingMethod.EXPRESS) {
            minDays = Math.max(0, minDays - 1);
            maxDays = Math.max(1, maxDays - 1);
        }

        LocalDate minDate = LocalDate.now().plusDays(minDays);
        LocalDate maxDate = LocalDate.now().plusDays(maxDays);

        String estimateLabel = minDays == maxDays
                ? String.format("Estimated: %s", minDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                : String.format("Estimated: %s - %s",
                minDate.format(DateTimeFormatter.ofPattern("MMM dd")),
                maxDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        return DeliveryChargeResponse.builder()
                .zoneName(zone != null ? zone.getName() : "Standard Zone")
                .shippingMethod(request.getShippingMethod())
                .baseCharge(baseCharge)
                .expressSurcharge(expressSurcharge)
                .codFee(codFee)
                .totalDeliveryCharge(totalCharge)
                .estimatedMinDate(minDate)
                .estimatedMaxDate(maxDate)
                .deliveryEstimateLabel(estimateLabel)
                .build();
    }

    @Override
    public DeliveryShipmentResponse createShipment(CreateShipmentRequest request) {
        log.info("Creating shipment for orderId: {}", request.getOrderId());

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", request.getOrderId()));

        if (shipmentRepository.findByOrderIdAndDeletedFalse(request.getOrderId()).isPresent()) {
            throw new BadRequestException("Shipment already exists for order ID: " + request.getOrderId());
        }

        DeliveryPartner partner = null;
        if (request.getPartnerId() != null) {
            partner = partnerRepository.findByIdAndDeletedFalse(request.getPartnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("DeliveryPartner", "id", request.getPartnerId()));
        }

        DeliveryZone zone = null;
        if (request.getZoneId() != null) {
            zone = zoneRepository.findByIdAndDeletedFalse(request.getZoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("DeliveryZone", "id", request.getZoneId()));
        }

        String trackingNumber = "TRK-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        String shipmentNumber = "SHP-" + System.currentTimeMillis();

        DeliveryChargeCalculateRequest calcRequest = DeliveryChargeCalculateRequest.builder()
                .zoneId(request.getZoneId())
                .shippingMethod(request.getShippingMethod())
                .isCod(request.getIsCod())
                .build();
        DeliveryChargeResponse chargeResponse = calculateDeliveryCharge(calcRequest);

        DeliveryShipment shipment = DeliveryShipment.builder()
                .shipmentNumber(shipmentNumber)
                .trackingNumber(trackingNumber)
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .partner(partner)
                .zone(zone)
                .shippingMethod(request.getShippingMethod())
                .status(DeliveryStatus.UNASSIGNED)
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .shippingAddress(request.getShippingAddress())
                .isCod(Boolean.TRUE.equals(request.getIsCod()))
                .codAmount(request.getCodAmount() != null ? request.getCodAmount() : BigDecimal.ZERO)
                .codFee(chargeResponse.getCodFee())
                .deliveryFee(chargeResponse.getTotalDeliveryCharge())
                .scheduledDate(request.getScheduledDate())
                .scheduledTimeSlot(request.getScheduledTimeSlot())
                .estimatedDeliveryDate(chargeResponse.getEstimatedMaxDate())
                .notes(request.getNotes())
                .build();

        shipment.addTimeline(DeliveryStatus.UNASSIGNED, "Fulfillment Warehouse", "Shipment created and awaiting rider dispatch", "SYSTEM");

        DeliveryShipment saved = shipmentRepository.save(shipment);
        log.info("Successfully created shipment ID: {}, trackingNumber: {}", saved.getId(), trackingNumber);

        return deliveryMapper.toShipmentResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryShipmentResponse getShipmentById(Long id) {
        DeliveryShipment shipment = shipmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryShipment", "id", id));
        return deliveryMapper.toShipmentResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryShipmentResponse getShipmentByOrderId(Long orderId) {
        DeliveryShipment shipment = shipmentRepository.findByOrderIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryShipment", "orderId", orderId));
        return deliveryMapper.toShipmentResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryTrackingResponse trackShipmentByTrackingNumber(String trackingNumber) {
        DeliveryShipment shipment = shipmentRepository.findByTrackingNumberAndDeletedFalse(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryShipment", "trackingNumber", trackingNumber));
        return deliveryMapper.toTrackingResponse(shipment);
    }

    @Override
    public DeliveryShipmentResponse assignRider(Long shipmentId, AssignRiderRequest request) {
        log.info("Assigning rider {} to shipment ID: {}", request.getRiderName(), shipmentId);
        DeliveryShipment shipment = shipmentRepository.findByIdAndDeletedFalse(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryShipment", "id", shipmentId));

        shipment.setRiderName(request.getRiderName());
        shipment.setRiderPhone(request.getRiderPhone());
        shipment.setVehicleInfo(request.getVehicleInfo());
        shipment.setStatus(DeliveryStatus.ASSIGNED);

        shipment.addTimeline(DeliveryStatus.ASSIGNED, "Dispatch Hub",
                String.format("Assigned to rider %s (%s)", request.getRiderName(), request.getRiderPhone()), "ADMIN");

        DeliveryShipment updated = shipmentRepository.save(shipment);
        return deliveryMapper.toShipmentResponse(updated);
    }

    @Override
    public DeliveryShipmentResponse updateDeliveryStatus(Long shipmentId, UpdateDeliveryStatusRequest request, String updatedBy) {
        log.info("Updating status for shipment ID: {} to {}", shipmentId, request.getStatus());
        DeliveryShipment shipment = shipmentRepository.findByIdAndDeletedFalse(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryShipment", "id", shipmentId));

        shipment.setStatus(request.getStatus());
        if (request.getStatus() == DeliveryStatus.DELIVERED) {
            shipment.setDeliveredAt(Instant.now());
        }

        shipment.addTimeline(request.getStatus(), request.getLocation(), request.getNote(), updatedBy);

        DeliveryShipment updated = shipmentRepository.save(shipment);
        return deliveryMapper.toShipmentResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DeliveryShipmentResponse> getShipmentsByStatus(DeliveryStatus status, Pageable pageable) {
        Page<DeliveryShipment> page = shipmentRepository.findByStatusAndDeletedFalse(status, pageable);
        return PageResponse.from(page, deliveryMapper::toShipmentResponse);
    }

    @Override
    public DeliveryPartnerResponse createPartner(DeliveryPartnerRequest request) {
        if (partnerRepository.existsByCodeAndDeletedFalse(request.getCode())) {
            throw new BadRequestException("Delivery partner code already exists: " + request.getCode());
        }
        DeliveryPartner partner = deliveryMapper.toPartnerEntity(request);
        DeliveryPartner saved = partnerRepository.save(partner);
        return deliveryMapper.toPartnerResponse(saved);
    }

    @Override
    public DeliveryPartnerResponse updatePartner(Long id, DeliveryPartnerRequest request) {
        DeliveryPartner partner = partnerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryPartner", "id", id));
        deliveryMapper.updatePartnerFromRequest(request, partner);
        DeliveryPartner updated = partnerRepository.save(partner);
        return deliveryMapper.toPartnerResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryPartnerResponse> getAllActivePartners() {
        List<DeliveryPartner> list = partnerRepository.findByActiveTrueAndDeletedFalse();
        return deliveryMapper.toPartnerResponseList(list);
    }

    @Override
    public DeliveryZoneResponse createZone(DeliveryZoneRequest request) {
        if (zoneRepository.existsByCodeAndDeletedFalse(request.getCode())) {
            throw new BadRequestException("Delivery zone code already exists: " + request.getCode());
        }
        DeliveryZone zone = deliveryMapper.toZoneEntity(request);
        DeliveryZone saved = zoneRepository.save(zone);
        return deliveryMapper.toZoneResponse(saved);
    }

    @Override
    public DeliveryZoneResponse updateZone(Long id, DeliveryZoneRequest request) {
        DeliveryZone zone = zoneRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryZone", "id", id));
        deliveryMapper.updateZoneFromRequest(request, zone);
        DeliveryZone updated = zoneRepository.save(zone);
        return deliveryMapper.toZoneResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryZoneResponse> getAllActiveZones() {
        List<DeliveryZone> list = zoneRepository.findByActiveTrueAndDeletedFalse();
        return deliveryMapper.toZoneResponseList(list);
    }

    private DeliveryZone resolveZone(Long zoneId, String division, String district) {
        if (zoneId != null) {
            Optional<DeliveryZone> opt = zoneRepository.findByIdAndDeletedFalse(zoneId);
            if (opt.isPresent()) return opt.get();
        }
        if (division != null || district != null) {
            Optional<DeliveryZone> opt = zoneRepository.findMatchingZone(division, district);
            if (opt.isPresent()) return opt.get();
        }
        List<DeliveryZone> activeZones = zoneRepository.findByActiveTrueAndDeletedFalse();
        return activeZones.isEmpty() ? null : activeZones.get(0);
    }
}
