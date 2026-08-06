package com.example.ecommerce.order.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.order.dto.request.AddOrderNoteRequest;
import com.example.ecommerce.order.dto.request.CheckoutRequest;
import com.example.ecommerce.order.dto.request.OrderFilterRequest;
import com.example.ecommerce.order.dto.request.UpdateOrderStatusRequest;
import com.example.ecommerce.order.dto.response.InvoiceResponse;
import com.example.ecommerce.order.dto.response.OrderResponse;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for order placement checkout, lifecycle status transitions,
 * invoice generation, order note appending and paginated history queries.
 */
public interface OrderService {

    OrderResponse checkout(CheckoutRequest request);

    OrderResponse getOrderById(Long id);

    OrderResponse getOrderByOrderNumber(String orderNumber);

    PageResponse<OrderResponse> getMyOrders(Pageable pageable);

    PageResponse<OrderResponse> getAllOrders(OrderFilterRequest filter, Pageable pageable);

    OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request);

    OrderResponse cancelOrder(Long id, String reason);

    OrderResponse addOrderNote(Long id, AddOrderNoteRequest request);

    InvoiceResponse getOrderInvoice(Long id);
}
