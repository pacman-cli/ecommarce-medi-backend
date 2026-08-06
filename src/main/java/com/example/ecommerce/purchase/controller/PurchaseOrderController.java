package com.example.ecommerce.purchase.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.purchase.dto.request.CreatePurchaseOrderRequest;
import com.example.ecommerce.purchase.dto.request.PurchaseOrderFilterRequest;
import com.example.ecommerce.purchase.dto.request.ReceivePurchaseItemsRequest;
import com.example.ecommerce.purchase.dto.request.RecordPurchasePaymentRequest;
import com.example.ecommerce.purchase.dto.request.UpdatePurchaseOrderRequest;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderListResponse;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderResponse;
import com.example.ecommerce.purchase.service.PurchaseOrderService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing purchase orders, procurement workflows, item receiving with inventory stock batching,
 * and invoice payment recordings.
 */
@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Purchase & Procurement", description = "Endpoints for managing purchase orders, supplier procurement, warehouse receiving, inventory stock batching, and invoice payments")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    @Operation(summary = "Create purchase order", description = "Initializes a new purchase order in DRAFT status with supplier, warehouse, and line items")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Purchase order created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid supplier, warehouse, or line items payload")
    })
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> createPurchaseOrder(
            @Valid @RequestBody CreatePurchaseOrderRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.createPurchaseOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Purchase order created successfully"));
    }

    @GetMapping
    @Operation(summary = "Search purchase orders (Paginated)", description = "Retrieves paginated list of purchase orders matching search specification filters")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseOrderListResponse>>> getPurchaseOrders(
            PurchaseOrderFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<PurchaseOrderListResponse> page = purchaseOrderService.getPurchaseOrders(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Purchase orders retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order by ID", description = "Retrieves complete purchase order details and line items by ID")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> getPurchaseOrderById(
            @Parameter(description = "Purchase Order ID", required = true) @PathVariable Long id) {
        PurchaseOrderResponse response = purchaseOrderService.getPurchaseOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Purchase order details retrieved successfully"));
    }

    @GetMapping("/number/{poNumber}")
    @Operation(summary = "Get purchase order by PO Number", description = "Retrieves purchase order details by unique PO number code")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> getPurchaseOrderByPoNumber(
            @Parameter(description = "PO Number", required = true) @PathVariable String poNumber) {
        PurchaseOrderResponse response = purchaseOrderService.getPurchaseOrderByPoNumber(poNumber);
        return ResponseEntity.ok(ApiResponse.success(response, "Purchase order details retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update DRAFT purchase order", description = "Updates an existing purchase order while in DRAFT status")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> updatePurchaseOrder(
            @Parameter(description = "Purchase Order ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdatePurchaseOrderRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.updatePurchaseOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Purchase order updated successfully"));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit purchase order", description = "Submits DRAFT purchase order and transitions status to ORDERED")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> submitPurchaseOrder(
            @Parameter(description = "Purchase Order ID", required = true) @PathVariable Long id) {
        PurchaseOrderResponse response = purchaseOrderService.submitPurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Purchase order submitted successfully"));
    }

    @PostMapping("/{id}/receive")
    @Operation(summary = "Receive purchase items", description = "Receives shipment quantities for items and provisions/increments inventory StockBatch records in destination warehouse")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> receivePurchaseItems(
            @Parameter(description = "Purchase Order ID", required = true) @PathVariable Long id,
            @Valid @RequestBody ReceivePurchaseItemsRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.receivePurchaseItems(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Purchase items received and inventory stock batches updated"));
    }

    @PostMapping("/{id}/payment")
    @Operation(summary = "Record invoice payment", description = "Records invoice payment towards purchase order total amount and updates payment status")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> recordPurchasePayment(
            @Parameter(description = "Purchase Order ID", required = true) @PathVariable Long id,
            @Valid @RequestBody RecordPurchasePaymentRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.recordPurchasePayment(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Purchase payment recorded successfully"));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel purchase order", description = "Cancels a purchase order if not yet fully received")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> cancelPurchaseOrder(
            @Parameter(description = "Purchase Order ID", required = true) @PathVariable Long id) {
        PurchaseOrderResponse response = purchaseOrderService.cancelPurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Purchase order cancelled successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete purchase order", description = "Soft deletes a purchase order")
    public ResponseEntity<ApiResponse<Void>> deletePurchaseOrder(
            @Parameter(description = "Purchase Order ID", required = true) @PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Purchase order deleted successfully"));
    }
}
