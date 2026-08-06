package com.example.ecommerce.product.controller;

import com.example.ecommerce.config.BaseControllerTest;
import com.example.ecommerce.product.dto.response.ProductResponse;
import com.example.ecommerce.product.entity.ProductStatus;
import com.example.ecommerce.product.entity.StockStatus;
import com.example.ecommerce.product.service.ProductService;
import com.example.ecommerce.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.ecommerce.config.properties.CorsProperties;
import com.example.ecommerce.security.TokenBlacklistService;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductControllerTest extends BaseControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @MockBean
    private CorsProperties corsProperties;

    @Test
    @WithMockUser
    void testGetProductByIdSuccess() throws Exception {
        ProductResponse response = ProductResponse.builder()
                .id(100L)
                .name("Test Product")
                .sku("SKU-100")
                .sellingPrice(new BigDecimal("99.99"))
                .status(ProductStatus.ACTIVE)
                .stockStatus(StockStatus.IN_STOCK)
                .build();

        when(productService.getProductById(100L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/products/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(100))
                .andExpect(jsonPath("$.data.sku").value("SKU-100"));
    }
}
