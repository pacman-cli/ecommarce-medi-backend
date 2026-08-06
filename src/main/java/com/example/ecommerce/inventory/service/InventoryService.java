package com.example.ecommerce.inventory.service;

import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.inventory.dto.request.InventoryAdjustmentRequest;
import com.example.ecommerce.inventory.dto.request.InventoryFilterRequest;
import com.example.ecommerce.inventory.dto.request.StockBatchRequest;
import com.example.ecommerce.inventory.dto.response.InventoryAlertResponse;
import com.example.ecommerce.inventory.dto.response.InventoryTransactionResponse;
import com.example.ecommerce.inventory.dto.response.StockBatchResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface managing stock batch intake, manual inventory adjustments, barcode/QR lookups,
 * transactional audit logs, and stock alerts.
 */
public interface InventoryService {

    StockBatchResponse receiveStockBatch(StockBatchRequest request);

    StockBatchResponse adjustStock(InventoryAdjustmentRequest request);

    StockBatchResponse getBatchById(Long id);

    StockBatchResponse getBatchByBarcode(String barcode);

    StockBatchResponse getBatchByQrCode(String qrCode);

    PageResponse<StockBatchResponse> getStockBatches(InventoryFilterRequest filter, Pageable pageable);

    PageResponse<InventoryTransactionResponse> getTransactionHistory(InventoryFilterRequest filter, Pageable pageable);

    List<InventoryAlertResponse> getLowStockAlerts();

    List<InventoryAlertResponse> getOutOfStockAlerts();

    List<StockBatchResponse> getExpiredBatches();
}
