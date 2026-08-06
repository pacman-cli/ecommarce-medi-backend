package com.example.ecommerce.delivery.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
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
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing delivery logistics, partners, zones, rate calculations,
 * rider assignments, tracking updates, and shipment fulfillment states.
 */
public interface DeliveryService {

    /**
     * Calculates delivery charges, COD handling fees, and estimated arrival date range.
     */
    DeliveryChargeResponse calculateDeliveryCharge(DeliveryChargeCalculateRequest request);

    /**
     * Initializes and creates a new shipment for an order.
     */
    DeliveryShipmentResponse createShipment(CreateShipmentRequest request);

    /**
     * Retrieves shipment details by ID.
     */
    DeliveryShipmentResponse getShipmentById(Long id);

    /**
     * Retrieves shipment details by Order ID.
     */
    DeliveryShipmentResponse getShipmentByOrderId(Long orderId);

    /**
     * Public tracking search by unique tracking number code.
     */
    DeliveryTrackingResponse trackShipmentByTrackingNumber(String trackingNumber);

    /**
     * Assigns a delivery rider to an existing shipment.
     */
    DeliveryShipmentResponse assignRider(Long shipmentId, AssignRiderRequest request);

    /**
     * Updates shipment delivery status and appends timeline log.
     */
    DeliveryShipmentResponse updateDeliveryStatus(Long shipmentId, UpdateDeliveryStatusRequest request, String updatedBy);

    /**
     * Retrieves paginated shipments by status filter.
     */
    PageResponse<DeliveryShipmentResponse> getShipmentsByStatus(DeliveryStatus status, Pageable pageable);

    /**
     * Creates a new delivery partner carrier.
     */
    DeliveryPartnerResponse createPartner(DeliveryPartnerRequest request);

    /**
     * Updates an existing delivery partner carrier.
     */
    DeliveryPartnerResponse updatePartner(Long id, DeliveryPartnerRequest request);

    /**
     * Retrieves all active delivery partner carriers.
     */
    List<DeliveryPartnerResponse> getAllActivePartners();

    /**
     * Creates a new delivery zone.
     */
    DeliveryZoneResponse createZone(DeliveryZoneRequest request);

    /**
     * Updates an existing delivery zone.
     */
    DeliveryZoneResponse updateZone(Long id, DeliveryZoneRequest request);

    /**
     * Retrieves all active delivery zones.
     */
    List<DeliveryZoneResponse> getAllActiveZones();
}
