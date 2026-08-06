package com.example.ecommerce.cart.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import org.springframework.stereotype.Component;

/**
 * Validates product availability, active status and stock quantities for shopping cart operations.
 */
@Component
public class CartValidator {

    /**
     * Validates product availability and stock quantity before adding or updating cart line items.
     *
     * @param product           target product
     * @param requestedQuantity quantity desired in cart
     */
    public void validateProductStock(Product product, int requestedQuantity) {
        if (product == null || product.isDeleted()) {
            throw new BadRequestException("Product is unavailable or no longer exists");
        }
        if (!product.isActive() || product.getStatus() != ProductStatus.ACTIVE) {
            throw new BadRequestException("Product '" + product.getName() + "' is currently inactive and cannot be purchased");
        }
        if (product.getStockStatus() == StockStatus.OUT_OF_STOCK || product.getQuantity() == null || product.getQuantity() <= 0) {
            throw new BadRequestException("Product '" + product.getName() + "' is out of stock");
        }
        if (requestedQuantity > product.getQuantity()) {
            throw new BadRequestException("Requested quantity (" + requestedQuantity + ") exceeds available stock (" + product.getQuantity() + ") for product '" + product.getName() + "'");
        }
    }
}
