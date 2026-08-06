package com.example.ecommerce.purchase.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.purchase.dto.request.CreatePurchaseOrderRequest;
import com.example.ecommerce.purchase.dto.request.PurchaseOrderFilterRequest;
import com.example.ecommerce.purchase.dto.request.ReceivePurchaseItemsRequest;
import com.example.ecommerce.purchase.dto.request.RecordPurchasePaymentRequest;
import com.example.ecommerce.purchase.dto.request.UpdatePurchaseOrderRequest;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderListResponse;
import com.example.ecommerce.purchase.dto.response.PurchaseOrderResponse;
import org.springframework.data.domain.Pageable;

/**
 * Service interface defining business operations for purchase order procurement,
 * item receiving with inventory stock batching, invoice payments, and lifecycle tracking.
 */
public interface PurchaseOrderService {

    /**
     * Initializes and creates a new purchase order in DRAFT status.
     */
    PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request);

    /**
     * Updates an existing purchase order in DRAFT status.
     */
    PurchaseOrderResponse updatePurchaseOrder(Long id, UpdatePurchaseOrderRequest request);

    /**
     * Retrieves purchase order by ID.
     */
    PurchaseOrderResponse getPurchaseOrderById(Long id);

    /**
     * Retrieves purchase order by unique PO number code.
     */
    PurchaseOrderResponse getPurchaseOrderByPoNumber(String poNumber);

    /**
     * Retrieves paginated list of purchase orders matching search specification filters.
     */
    PageResponse<PurchaseOrderListResponse> getPurchaseOrders(PurchaseOrderFilterRequest filter, Pageable pageable);

    /**
     * Submits DRAFT purchase order transitioning status to ORDERED.
     */
    PurchaseOrderResponse submitPurchaseOrder(Long id);

    /**
     * Receives quantities for purchase items and provisions inventory StockBatch records.
     */
    PurchaseOrderResponse receivePurchaseItems(Long id, ReceivePurchaseItemsRequest request);

    /**
     * Records invoice payment towards purchase order total.
     */
    PurchaseOrderResponse recordPurchasePayment(Long id, RecordPurchasePaymentRequest request);

    /**
     * Cancels purchase order if not yet fully received.
     */
    PurchaseOrderResponse cancelPurchaseOrder(Long id);

    /**
     * Soft deletes purchase order.
     */
    void deletePurchaseOrder(Long id);
}
