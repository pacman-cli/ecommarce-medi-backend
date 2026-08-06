package com.example.ecommerce.delivery.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
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
import com.example.ecommerce.delivery.service.DeliveryService;
import com.example.ecommerce.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing endpoints for delivery rates, shipment tracking, rider dispatches,
 * status updates, partner carriers, and geographic delivery zones.
 */
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Tag(name = "Delivery & Logistics", description = "Endpoints for delivery rate estimation, order shipments, tracking, rider dispatches, carriers, and delivery zones")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/calculate-charge")
    @Operation(summary = "Calculate delivery charge & estimated dates", description = "Public endpoint to compute shipping charge rates, surcharges, and estimated delivery dates")
    public ResponseEntity<ApiResponse<DeliveryChargeResponse>> calculateDeliveryCharge(
            @Valid @RequestBody DeliveryChargeCalculateRequest request) {
        DeliveryChargeResponse response = deliveryService.calculateDeliveryCharge(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Delivery charge calculated successfully"));
    }

    @GetMapping("/track/{trackingNumber}")
    @Operation(summary = "Track shipment status", description = "Public endpoint to look up order shipment fulfillment timeline by tracking number")
    public ResponseEntity<ApiResponse<DeliveryTrackingResponse>> trackShipment(
            @Parameter(description = "Logistics tracking number code", required = true)
            @PathVariable String trackingNumber) {
        DeliveryTrackingResponse response = deliveryService.trackShipmentByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(ApiResponse.success(response, "Tracking status retrieved successfully"));
    }

    @PostMapping("/shipments")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create order shipment", description = "Initializes order shipment details, generates tracking number code, and records initial timeline entry")
    public ResponseEntity<ApiResponse<DeliveryShipmentResponse>> createShipment(
            @Valid @RequestBody CreateShipmentRequest request) {
        DeliveryShipmentResponse response = deliveryService.createShipment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Shipment created successfully"));
    }

    @GetMapping("/shipments/{id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get shipment details by ID", description = "Retrieves shipment details and full timeline audit log")
    public ResponseEntity<ApiResponse<DeliveryShipmentResponse>> getShipmentById(
            @Parameter(description = "Shipment ID", required = true) @PathVariable Long id) {
        DeliveryShipmentResponse response = deliveryService.getShipmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Shipment details retrieved successfully"));
    }

    @GetMapping("/shipments/order/{orderId}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get shipment by Order ID", description = "Retrieves shipment details associated with an order")
    public ResponseEntity<ApiResponse<DeliveryShipmentResponse>> getShipmentByOrderId(
            @Parameter(description = "Order ID", required = true) @PathVariable Long orderId) {
        DeliveryShipmentResponse response = deliveryService.getShipmentByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success(response, "Order shipment retrieved successfully"));
    }

    @PutMapping("/shipments/{id}/assign-rider")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Assign rider to shipment", description = "Dispatches delivery rider details (name, phone, vehicle info) to shipment")
    public ResponseEntity<ApiResponse<DeliveryShipmentResponse>> assignRider(
            @Parameter(description = "Shipment ID", required = true) @PathVariable Long id,
            @Valid @RequestBody AssignRiderRequest request) {
        DeliveryShipmentResponse response = deliveryService.assignRider(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Rider assigned successfully"));
    }

    @PutMapping("/shipments/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update shipment delivery status", description = "Updates fulfillment status and appends location checkpoint timeline entry")
    public ResponseEntity<ApiResponse<DeliveryShipmentResponse>> updateDeliveryStatus(
            @Parameter(description = "Shipment ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateDeliveryStatusRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String updatedBy = userPrincipal != null ? userPrincipal.getUsername() : "ADMIN";
        DeliveryShipmentResponse response = deliveryService.updateDeliveryStatus(id, request, updatedBy);
        return ResponseEntity.ok(ApiResponse.success(response, "Delivery status updated successfully"));
    }

    @GetMapping("/shipments/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get shipments by status (Paginated)", description = "Retrieves paginated shipments matching specified delivery status")
    public ResponseEntity<ApiResponse<PageResponse<DeliveryShipmentResponse>>> getShipmentsByStatus(
            @Parameter(description = "Delivery status", required = true) @PathVariable DeliveryStatus status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<DeliveryShipmentResponse> page = deliveryService.getShipmentsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Shipments retrieved successfully"));
    }

    @PostMapping("/partners")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create delivery partner carrier", description = "Creates a new logistics carrier partner entry")
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> createPartner(
            @Valid @RequestBody DeliveryPartnerRequest request) {
        DeliveryPartnerResponse response = deliveryService.createPartner(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Delivery partner created successfully"));
    }

    @PutMapping("/partners/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update delivery partner carrier", description = "Updates an existing logistics carrier partner")
    public ResponseEntity<ApiResponse<DeliveryPartnerResponse>> updatePartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Valid @RequestBody DeliveryPartnerRequest request) {
        DeliveryPartnerResponse response = deliveryService.updatePartner(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Delivery partner updated successfully"));
    }

    @GetMapping("/partners")
    @Operation(summary = "Get all active delivery partners", description = "Retrieves list of active logistics carrier partners")
    public ResponseEntity<ApiResponse<List<DeliveryPartnerResponse>>> getAllActivePartners() {
        List<DeliveryPartnerResponse> list = deliveryService.getAllActivePartners();
        return ResponseEntity.ok(ApiResponse.success(list, "Active delivery partners retrieved successfully"));
    }

    @PostMapping("/zones")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create delivery zone", description = "Creates a new geographic delivery zone with base rates and delivery estimates")
    public ResponseEntity<ApiResponse<DeliveryZoneResponse>> createZone(
            @Valid @RequestBody DeliveryZoneRequest request) {
        DeliveryZoneResponse response = deliveryService.createZone(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Delivery zone created successfully"));
    }

    @PutMapping("/zones/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update delivery zone", description = "Updates base rates, express fees, and delivery day ranges for a zone")
    public ResponseEntity<ApiResponse<DeliveryZoneResponse>> updateZone(
            @Parameter(description = "Zone ID", required = true) @PathVariable Long id,
            @Valid @RequestBody DeliveryZoneRequest request) {
        DeliveryZoneResponse response = deliveryService.updateZone(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Delivery zone updated successfully"));
    }

    @GetMapping("/zones")
    @Operation(summary = "Get all active delivery zones", description = "Retrieves list of active geographic delivery zones")
    public ResponseEntity<ApiResponse<List<DeliveryZoneResponse>>> getAllActiveZones() {
        List<DeliveryZoneResponse> list = deliveryService.getAllActiveZones();
        return ResponseEntity.ok(ApiResponse.success(list, "Active delivery zones retrieved successfully"));
    }
}
