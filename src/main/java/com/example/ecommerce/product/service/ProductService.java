package com.example.ecommerce.product.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.product.dto.request.ProductFilterRequest;
import com.example.ecommerce.product.dto.request.ProductRequest;
import com.example.ecommerce.product.dto.request.UpdateStockRequest;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.product.entity.ProductStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface defining business operations for product management.
 */
public interface ProductService {

    /**
     * Creates a new product.
     *
     * @param request creation request payload
     * @return created product response DTO
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * Updates an existing product by ID.
     *
     * @param id      product ID
     * @param request update payload
     * @return updated product response DTO
     */
    ProductResponse updateProduct(Long id, ProductRequest request);

    /**
     * Retrieves product by ID.
     *
     * @param id product ID
     * @return product response DTO
     */
    ProductResponse getProductById(Long id);

    /**
     * Retrieves product by URL slug.
     *
     * @param slug product slug
     * @return product response DTO
     */
    ProductResponse getProductBySlug(String slug);

    /**
     * Retrieves product by SKU.
     *
     * @param sku product SKU
     * @return product response DTO
     */
    ProductResponse getProductBySku(String sku);

    /**
     * Searches products with dynamic filtering, sorting and pagination.
     *
     * @param filter   filter criteria
     * @param pageable pagination parameters
     * @return paginated product response
     */
    PageResponse<ProductResponse> getProducts(ProductFilterRequest filter, Pageable pageable);

    /**
     * Updates inventory stock levels for a product.
     *
     * @param id      product ID
     * @param request stock update payload
     * @return updated product response DTO
     */
    ProductResponse updateStock(Long id, UpdateStockRequest request);

    /**
     * Updates product operational status.
     *
     * @param id     product ID
     * @param status new status
     * @return updated product response DTO
     */
    ProductResponse updateStatus(Long id, ProductStatus status);

    /**
     * Returns featured products.
     *
     * @return featured products list
     */
    List<ProductResponse> getFeaturedProducts();

    /**
     * Returns bestseller products.
     *
     * @return bestseller products list
     */
    List<ProductResponse> getBestsellerProducts();

    /**
     * Returns new arrival products.
     *
     * @return new arrival products list
     */
    List<ProductResponse> getNewArrivalProducts();

    /**
     * Returns trending products.
     *
     * @return trending products list
     */
    List<ProductResponse> getTrendingProducts();

    /**
     * Returns recommended products.
     *
     * @return recommended products list
     */
    List<ProductResponse> getRecommendedProducts();

    /**
     * Soft deletes product.
     *
     * @param id product ID
     */
    void deleteProduct(Long id);
}
