package com.example.ecommerce.dashboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Customer demography and spending analytics payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Customer statistics and user engagement payload")
public class CustomerAnalyticsResponse {

    @Schema(description = "Total registered customer accounts", example = "1250")
    private Long totalCustomers;

    @Schema(description = "New customer signups within active period", example = "85")
    private Long newCustomers;

    @Schema(description = "Active customers who placed an order within period", example = "140")
    private Long activeCustomers;

    @Schema(description = "Repeat purchase customer rate percentage", example = "38.5")
    private Double repeatPurchaseRate;

    @Schema(description = "Customer growth percentage versus prior comparison period", example = "12.4")
    private Double growthPercentage;

    @Schema(description = "List of top spending customers")
    private List<TopCustomerResponse> topCustomers;
}
