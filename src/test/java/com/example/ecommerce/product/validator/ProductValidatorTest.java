package com.example.ecommerce.product.validator;

import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductValidator productValidator;

    @Test
    @DisplayName("validateSkuUniqueness should throw ConflictException if duplicate SKU exists")
    void validateSkuUniqueness_Duplicate_ThrowsConflict() {
        when(productRepository.existsBySkuIgnoreCase("MED-PARA-500")).thenReturn(true);

        assertThatThrownBy(() -> productValidator.validateSkuUniqueness("MED-PARA-500", null))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Product SKU 'MED-PARA-500' already exists");
    }

    @Test
    @DisplayName("validatePricingAndStock should throw BadRequestException if discount higher than selling price")
    void validatePricingAndStock_InvalidDiscount_ThrowsBadRequest() {
        BigDecimal sellingPrice = new BigDecimal("10.00");
        BigDecimal discountPrice = new BigDecimal("15.00");

        assertThatThrownBy(() -> productValidator.validatePricingAndStock(null, sellingPrice, discountPrice, 10, 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Discount price cannot be higher than selling price");
    }

    @Test
    @DisplayName("validatePricingAndStock should throw BadRequestException if reserved quantity exceeds total stock")
    void validatePricingAndStock_ReservedExceedsQuantity_ThrowsBadRequest() {
        assertThatThrownBy(() -> productValidator.validatePricingAndStock(null, new BigDecimal("10.00"), null, 5, 10))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Reserved quantity cannot exceed total stock quantity");
    }

    @Test
    @DisplayName("validatePricingAndStock should pass for valid price and stock values")
    void validatePricingAndStock_Valid_Success() {
        assertThatCode(() -> productValidator.validatePricingAndStock(
                new BigDecimal("5.00"),
                new BigDecimal("10.00"),
                new BigDecimal("8.00"),
                100,
                5
        )).doesNotThrowAnyException();
    }
}
