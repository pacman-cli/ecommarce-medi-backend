package com.example.ecommerce.inventory.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.inventory.dto.request.InventoryAdjustmentRequest;
import com.example.ecommerce.inventory.dto.request.InventoryFilterRequest;
import com.example.ecommerce.inventory.dto.request.StockBatchRequest;
import com.example.ecommerce.inventory.dto.response.InventoryAlertResponse;
import com.example.ecommerce.inventory.dto.response.InventoryTransactionResponse;
import com.example.ecommerce.inventory.dto.response.StockBatchResponse;
import com.example.ecommerce.inventory.service.InventoryService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing endpoints for stock batch intake, manual adjustments,
 * barcode/QR code lookups, transactional history and stock alert reports.
 */
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "Endpoints for stock batches, adjustments, barcode/QR lookup, audit transactions and stock alerts")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/batches")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Receive stock batch", description = "Records inbound purchase stock batch intake (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Stock batch received successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid batch payload or date constraint failure"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate batch number in warehouse")
    })
    public ResponseEntity<ApiResponse<StockBatchResponse>> receiveStockBatch(
            @Valid @RequestBody StockBatchRequest request) {
        StockBatchResponse created = inventoryService.receiveStockBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Stock batch received successfully"));
    }

    @PostMapping("/adjustments")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Adjust inventory stock", description = "Performs stock increase, decrease, write-off or return adjustment (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inventory adjusted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Insufficient stock for decrease adjustment")
    })
    public ResponseEntity<ApiResponse<StockBatchResponse>> adjustStock(
            @Valid @RequestBody InventoryAdjustmentRequest request) {
        StockBatchResponse updated = inventoryService.adjustStock(request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Inventory adjusted successfully"));
    }

    @GetMapping("/batches")
    @Operation(summary = "Get stock batches", description = "Returns a paginated list of stock batches based on dynamic criteria")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock batches retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<StockBatchResponse>>> getStockBatches(
            @ModelAttribute InventoryFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<StockBatchResponse> page = inventoryService.getStockBatches(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Stock batches retrieved successfully"));
    }

    @GetMapping("/batches/{id}")
    @Operation(summary = "Get batch by ID", description = "Retrieves stock batch details by ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock batch retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Stock batch not found")
    })
    public ResponseEntity<ApiResponse<StockBatchResponse>> getBatchById(
            @Parameter(description = "Batch ID", required = true) @PathVariable Long id) {
        StockBatchResponse batch = inventoryService.getBatchById(id);
        return ResponseEntity.ok(ApiResponse.success(batch, "Stock batch retrieved successfully"));
    }

    @GetMapping("/batches/barcode/{barcode}")
    @Operation(summary = "Lookup batch by barcode", description = "Scans and retrieves stock batch by barcode")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock batch retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Stock batch not found")
    })
    public ResponseEntity<ApiResponse<StockBatchResponse>> getBatchByBarcode(
            @Parameter(description = "Barcode string", required = true) @PathVariable String barcode) {
        StockBatchResponse batch = inventoryService.getBatchByBarcode(barcode);
        return ResponseEntity.ok(ApiResponse.success(batch, "Stock batch retrieved successfully"));
    }

    @GetMapping("/batches/qr")
    @Operation(summary = "Lookup batch by QR code", description = "Scans and retrieves stock batch by QR code URL/string")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock batch retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Stock batch not found")
    })
    public ResponseEntity<ApiResponse<StockBatchResponse>> getBatchByQrCode(
            @Parameter(description = "QR Code string", required = true) @RequestParam String qrCode) {
        StockBatchResponse batch = inventoryService.getBatchByQrCode(qrCode);
        return ResponseEntity.ok(ApiResponse.success(batch, "Stock batch retrieved successfully"));
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get transaction history", description = "Returns paginated inventory audit transaction logs (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<InventoryTransactionResponse>>> getTransactionHistory(
            @ModelAttribute InventoryFilterRequest filter,
            @PageableDefault(sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<InventoryTransactionResponse> page = inventoryService.getTransactionHistory(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Transaction history retrieved successfully"));
    }

    @GetMapping("/alerts/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get low stock alerts", description = "Returns active products/batches reaching low stock thresholds (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Low stock alerts retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<InventoryAlertResponse>>> getLowStockAlerts() {
        List<InventoryAlertResponse> alerts = inventoryService.getLowStockAlerts();
        return ResponseEntity.ok(ApiResponse.success(alerts, "Low stock alerts retrieved successfully"));
    }

    @GetMapping("/alerts/out-of-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get out-of-stock alerts", description = "Returns products with 0 available stock quantity (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Out-of-stock alerts retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<InventoryAlertResponse>>> getOutOfStockAlerts() {
        List<InventoryAlertResponse> alerts = inventoryService.getOutOfStockAlerts();
        return ResponseEntity.ok(ApiResponse.success(alerts, "Out-of-stock alerts retrieved successfully"));
    }

    @GetMapping("/alerts/expired")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get expired batches", description = "Returns stock batches whose expiration date has passed (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Expired batches retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<StockBatchResponse>>> getExpiredBatches() {
        List<StockBatchResponse> expired = inventoryService.getExpiredBatches();
        return ResponseEntity.ok(ApiResponse.success(expired, "Expired batches retrieved successfully"));
    }
}
