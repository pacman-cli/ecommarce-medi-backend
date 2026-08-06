package com.example.ecommerce.delivery.service;

import com.example.ecommerce.delivery.dto.enums.DeliveryStatus;
import com.example.ecommerce.delivery.dto.enums.ShippingMethod;
import com.example.ecommerce.delivery.dto.request.AssignRiderRequest;
import com.example.ecommerce.delivery.dto.request.CreateShipmentRequest;
import com.example.ecommerce.delivery.dto.request.DeliveryChargeCalculateRequest;
import com.example.ecommerce.delivery.dto.request.UpdateDeliveryStatusRequest;
import com.example.ecommerce.delivery.dto.response.DeliveryChargeResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryShipmentResponse;
import com.example.ecommerce.delivery.dto.response.DeliveryTrackingResponse;
import com.example.ecommerce.delivery.entity.DeliveryShipment;
import com.example.ecommerce.delivery.entity.DeliveryZone;
import com.example.ecommerce.delivery.mapper.DeliveryMapper;
import com.example.ecommerce.delivery.repository.DeliveryPartnerRepository;
import com.example.ecommerce.delivery.repository.DeliveryShipmentRepository;
import com.example.ecommerce.delivery.repository.DeliveryZoneRepository;
import com.example.ecommerce.delivery.service.impl.DeliveryServiceImpl;
import com.example.ecommerce.order.entity.Order;
import com.example.ecommerce.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock
    private DeliveryPartnerRepository partnerRepository;

    @Mock
    private DeliveryZoneRepository zoneRepository;

    @Mock
    private DeliveryShipmentRepository shipmentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DeliveryMapper deliveryMapper;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    private DeliveryZone sampleZone;
    private Order sampleOrder;
    private DeliveryShipment sampleShipment;
    private DeliveryShipmentResponse sampleResponse;
    private DeliveryTrackingResponse sampleTrackingResponse;

    @BeforeEach
    void setUp() {
        sampleZone = DeliveryZone.builder()
                .name("Inside Dhaka City")
                .code("INSIDE_DHAKA")
                .division("Dhaka")
                .district("Dhaka")
                .baseFee(new BigDecimal("60.00"))
                .expressFee(new BigDecimal("120.00"))
                .codFee(new BigDecimal("15.00"))
                .minDeliveryDays(1)
                .maxDeliveryDays(2)
                .build();
        sampleZone.setId(1L);

        sampleOrder = Order.builder()
                .orderNumber("ORD-20260805-001")
                .build();
        sampleOrder.setId(501L);

        sampleShipment = DeliveryShipment.builder()
                .shipmentNumber("SHP-20260805-00101")
                .trackingNumber("TRK-20260805-98421")
                .orderId(501L)
                .orderNumber("ORD-20260805-001")
                .shippingMethod(ShippingMethod.STANDARD)
                .status(DeliveryStatus.UNASSIGNED)
                .recipientName("Jane Doe")
                .recipientPhone("+8801700000000")
                .shippingAddress("Dhanmondi, Dhaka")
                .build();
        sampleShipment.setId(101L);

        sampleResponse = DeliveryShipmentResponse.builder()
                .id(101L)
                .trackingNumber("TRK-20260805-98421")
                .orderId(501L)
                .status(DeliveryStatus.UNASSIGNED)
                .shippingMethod(ShippingMethod.STANDARD)
                .build();

        sampleTrackingResponse = DeliveryTrackingResponse.builder()
                .trackingNumber("TRK-20260805-98421")
                .currentStatus(DeliveryStatus.UNASSIGNED)
                .shippingMethod(ShippingMethod.STANDARD)
                .build();
    }

    @Test
    void testCalculateDeliveryChargeStandard() {
        when(zoneRepository.findByIdAndDeletedFalse(eq(1L))).thenReturn(Optional.of(sampleZone));

        DeliveryChargeCalculateRequest request = DeliveryChargeCalculateRequest.builder()
                .zoneId(1L)
                .shippingMethod(ShippingMethod.STANDARD)
                .isCod(false)
                .build();

        DeliveryChargeResponse response = deliveryService.calculateDeliveryCharge(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("60.00"), response.getTotalDeliveryCharge());
        assertEquals(ShippingMethod.STANDARD, response.getShippingMethod());
    }

    @Test
    void testCalculateDeliveryChargeExpressWithCod() {
        when(zoneRepository.findByIdAndDeletedFalse(eq(1L))).thenReturn(Optional.of(sampleZone));

        DeliveryChargeCalculateRequest request = DeliveryChargeCalculateRequest.builder()
                .zoneId(1L)
                .shippingMethod(ShippingMethod.EXPRESS)
                .isCod(true)
                .build();

        DeliveryChargeResponse response = deliveryService.calculateDeliveryCharge(request);

        assertNotNull(response);
        // Base(60) + Express(120) + COD(15) = 195.00
        assertEquals(new BigDecimal("195.00"), response.getTotalDeliveryCharge());
    }

    @Test
    void testCreateShipmentSuccess() {
        when(orderRepository.findById(eq(501L))).thenReturn(Optional.of(sampleOrder));
        when(shipmentRepository.findByOrderIdAndDeletedFalse(eq(501L))).thenReturn(Optional.empty());
        when(shipmentRepository.save(any(DeliveryShipment.class))).thenReturn(sampleShipment);
        when(deliveryMapper.toShipmentResponse(any(DeliveryShipment.class))).thenReturn(sampleResponse);

        CreateShipmentRequest request = CreateShipmentRequest.builder()
                .orderId(501L)
                .shippingMethod(ShippingMethod.STANDARD)
                .recipientName("Jane Doe")
                .recipientPhone("+8801700000000")
                .shippingAddress("Dhanmondi, Dhaka")
                .build();

        DeliveryShipmentResponse response = deliveryService.createShipment(request);

        assertNotNull(response);
        assertEquals(101L, response.getId());
        verify(shipmentRepository, times(1)).save(any(DeliveryShipment.class));
    }

    @Test
    void testAssignRiderSuccess() {
        when(shipmentRepository.findByIdAndDeletedFalse(eq(101L))).thenReturn(Optional.of(sampleShipment));
        when(shipmentRepository.save(any(DeliveryShipment.class))).thenReturn(sampleShipment);
        when(deliveryMapper.toShipmentResponse(any(DeliveryShipment.class))).thenReturn(sampleResponse);

        AssignRiderRequest request = AssignRiderRequest.builder()
                .riderName("Rahim Uddin")
                .riderPhone("+8801711223344")
                .vehicleInfo("Motorbike")
                .build();

        DeliveryShipmentResponse response = deliveryService.assignRider(101L, request);

        assertNotNull(response);
        assertEquals("Rahim Uddin", sampleShipment.getRiderName());
        assertEquals(DeliveryStatus.ASSIGNED, sampleShipment.getStatus());
    }

    @Test
    void testUpdateDeliveryStatusToDelivered() {
        when(shipmentRepository.findByIdAndDeletedFalse(eq(101L))).thenReturn(Optional.of(sampleShipment));
        when(shipmentRepository.save(any(DeliveryShipment.class))).thenReturn(sampleShipment);
        when(deliveryMapper.toShipmentResponse(any(DeliveryShipment.class))).thenReturn(sampleResponse);

        UpdateDeliveryStatusRequest request = UpdateDeliveryStatusRequest.builder()
                .status(DeliveryStatus.DELIVERED)
                .location("Customer Doorstep")
                .note("Package delivered and signed")
                .build();

        DeliveryShipmentResponse response = deliveryService.updateDeliveryStatus(101L, request, "RIDER");

        assertNotNull(response);
        assertEquals(DeliveryStatus.DELIVERED, sampleShipment.getStatus());
        assertNotNull(sampleShipment.getDeliveredAt());
    }

    @Test
    void testTrackShipmentByTrackingNumber() {
        when(shipmentRepository.findByTrackingNumberAndDeletedFalse(eq("TRK-20260805-98421")))
                .thenReturn(Optional.of(sampleShipment));
        when(deliveryMapper.toTrackingResponse(eq(sampleShipment))).thenReturn(sampleTrackingResponse);

        DeliveryTrackingResponse response = deliveryService.trackShipmentByTrackingNumber("TRK-20260805-98421");

        assertNotNull(response);
        assertEquals("TRK-20260805-98421", response.getTrackingNumber());
    }
}
