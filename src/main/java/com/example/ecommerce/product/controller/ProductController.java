package com.example.ecommerce.product.controller;

import com.example.ecommerce.common.dto.response.ApiResponse;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.request.ProductFilterRequest;
import com.example.ecommerce.product.dto.request.ProductRequest;
import com.example.ecommerce.product.dto.request.UpdateStockRequest;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.service.ProductService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing endpoints for product search, dynamic multi-criteria filtering,
 * stock management, promotional collections and CRUD operations.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "Endpoints for managing products, full-text search, inventory stock, media and promotional lists")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Search and filter products", description = "Returns a paginated list of products based on multi-criteria filter parameters")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @ModelAttribute ProductFilterRequest filter,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ProductResponse> page = productService.getProducts(filter, pageable);
        return ResponseEntity.ok(ApiResponse.success(page, "Products retrieved successfully"));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured products", description = "Returns active products flagged as featured")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Featured products retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {
        List<ProductResponse> featured = productService.getFeaturedProducts();
        return ResponseEntity.ok(ApiResponse.success(featured, "Featured products retrieved successfully"));
    }

    @GetMapping("/bestsellers")
    @Operation(summary = "Get bestseller products", description = "Returns active products flagged as bestsellers")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bestseller products retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getBestsellerProducts() {
        List<ProductResponse> bestsellers = productService.getBestsellerProducts();
        return ResponseEntity.ok(ApiResponse.success(bestsellers, "Bestseller products retrieved successfully"));
    }

    @GetMapping("/new-arrivals")
    @Operation(summary = "Get new arrival products", description = "Returns active products flagged as new arrivals")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "New arrivals retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getNewArrivalProducts() {
        List<ProductResponse> newArrivals = productService.getNewArrivalProducts();
        return ResponseEntity.ok(ApiResponse.success(newArrivals, "New arrivals retrieved successfully"));
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending products", description = "Returns active products flagged as trending")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trending products retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getTrendingProducts() {
        List<ProductResponse> trending = productService.getTrendingProducts();
        return ResponseEntity.ok(ApiResponse.success(trending, "Trending products retrieved successfully"));
    }

    @GetMapping("/recommended")
    @Operation(summary = "Get recommended products", description = "Returns active products flagged as recommended")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recommended products retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getRecommendedProducts() {
        List<ProductResponse> recommended = productService.getRecommendedProducts();
        return ResponseEntity.ok(ApiResponse.success(recommended, "Recommended products retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves product details by unique database ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get product by URL slug", description = "Retrieves product details by URL slug")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySlug(
            @Parameter(description = "Product slug", required = true) @PathVariable String slug) {
        ProductResponse product = productService.getProductBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU", description = "Retrieves product details by SKU code")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySku(
            @Parameter(description = "Product SKU", required = true) @PathVariable String sku) {
        ProductResponse product = productService.getProductBySku(sku);
        return ResponseEntity.ok(ApiResponse.success(product, "Product retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create product", description = "Creates a new product with complete pricing, inventory, medicine specs and media (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload or price/stock invariant failure"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "SKU, barcode, name or slug conflict")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Product created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product", description = "Updates an existing product (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "SKU, barcode, name or slug conflict")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product updated successfully"));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update stock levels", description = "Updates inventory stock quantity, reserved quantity and low stock threshold (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid stock quantity"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> updateStock(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateStockRequest request) {
        ProductResponse updated = productService.updateStock(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product stock updated successfully"));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product status", description = "Toggles product operational status (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<ProductResponse>> updateStatus(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Parameter(description = "New Product Status", required = true) @RequestParam ProductStatus status) {
        ProductResponse updated = productService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product status updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete product", description = "Soft deletes a product from catalogue (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product soft deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product soft deleted successfully"));
    }
}
