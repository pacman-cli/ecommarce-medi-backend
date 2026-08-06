package com.example.ecommerce.order.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.order.dto.request.AddOrderNoteRequest;
import com.example.ecommerce.order.dto.request.CheckoutRequest;
import com.example.ecommerce.order.dto.request.OrderFilterRequest;
import com.example.ecommerce.order.dto.request.UpdateOrderStatusRequest;
import com.example.ecommerce.order.dto.response.InvoiceResponse;
import com.example.ecommerce.order.dto.response.OrderResponse;
import com.example.ecommerce.order.service.OrderService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing endpoints for order placement checkout, status updates,
 * tracking numbers, order timeline logs and tax invoices.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for checkout, order placement, status timelines, invoices, tracking numbers and history")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @Operation(summary = "Checkout & place order", description = "Converts an active guest or user shopping cart into a placed order with address snapshots, stock deductions and invoice generation")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Empty cart, insufficient stock or invalid address details")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> checkout(
            @Valid @RequestBody CheckoutRequest request) {
        OrderResponse response = orderService.checkout(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Order placed successfully"));
    }

    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get user order history", description = "Retrieves paginated list of orders placed by the authenticated user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User order history retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getMyOrders(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<OrderResponse> page = orderService.getMyOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "User order history retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves order details, line items and status timeline history by order ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Order details retrieved successfully"));
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Retrieves order details using unique order tracking code (e.g. ORD-20260804-98421)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByOrderNumber(
            @Parameter(description = "Order number code", required = true) @PathVariable String orderNumber) {
        OrderResponse response = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(response, "Order details retrieved successfully"));
    }

    @GetMapping("/{id}/invoice")
    @Operation(summary = "Get order tax invoice", description = "Generates and retrieves invoice snapshot DTO for an order")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order invoice retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ApiResponse<InvoiceResponse>> getOrderInvoice(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id) {
        InvoiceResponse invoice = orderService.getOrderInvoice(id);
        return ResponseEntity.ok(ApiResponse.success(invoice, "Order invoice retrieved successfully"));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancels a pending or confirmed order and restores inventory quantities")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Order cannot be cancelled in its current state")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id,
            @Parameter(description = "Reason for cancellation") @RequestParam(required = false) String reason) {
        OrderResponse response = orderService.cancelOrder(id, reason);
        return ResponseEntity.ok(ApiResponse.success(response, "Order cancelled successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all orders", description = "Retrieves paginated master listing of orders with dynamic status and date filtering (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All orders retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getAllOrders(
            @ModelAttribute OrderFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<OrderResponse> page = orderService.getAllOrders(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "All orders retrieved successfully"));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update order status", description = "Transitions order status (e.g. PACKED, SHIPPED, DELIVERED), assigns tracking numbers and appends timeline log (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid status transition")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Order status updated successfully"));
    }

    @PostMapping("/{id}/notes")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add admin order note", description = "Appends an administrative note to the order timeline (Admin only)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order note added successfully")
    })
    public ResponseEntity<ApiResponse<OrderResponse>> addOrderNote(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id,
            @Valid @RequestBody AddOrderNoteRequest request) {
        OrderResponse response = orderService.addOrderNote(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Order note added successfully"));
    }
}
