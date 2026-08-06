package com.example.ecommerce.address.service;

import com.example.ecommerce.address.dto.enums.AddressType;
import com.example.ecommerce.address.dto.request.AddressRequest;
import com.example.ecommerce.address.dto.response.AddressResponse;
import com.example.ecommerce.address.mapper.AddressMapper;
import com.example.ecommerce.address.repository.AddressRepository;
import com.example.ecommerce.address.service.impl.AddressServiceImpl;
import com.example.ecommerce.address.validator.AddressValidator;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.user.entity.Address;
import com.example.ecommerce.user.entity.User;
import com.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressMapper addressMapper;

    @Spy
    private AddressValidator addressValidator;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User sampleUser;
    private Address sampleAddress;
    private AddressRequest sampleRequest;
    private AddressResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().email("customer@example.com").build();
        sampleUser.setId(1L);

        sampleAddress = Address.builder()
                .user(sampleUser)
                .label("HOME")
                .addressType(AddressType.SHIPPING)
                .recipientName("Jane Doe")
                .phone("+8801700000000")
                .street("Road 5, House 12")
                .city("Dhaka")
                .country("Bangladesh")
                .isDefault(true)
                .defaultShipping(true)
                .build();
        sampleAddress.setId(10L);

        sampleRequest = AddressRequest.builder()
                .label("HOME")
                .addressType(AddressType.SHIPPING)
                .recipientName("Jane Doe")
                .phone("+8801700000000")
                .street("Road 5, House 12")
                .city("Dhaka")
                .country("Bangladesh")
                .defaultShipping(true)
                .build();

        sampleResponse = AddressResponse.builder()
                .id(10L)
                .userId(1L)
                .label("HOME")
                .addressType(AddressType.SHIPPING)
                .recipientName("Jane Doe")
                .phone("+8801700000000")
                .street("Road 5, House 12")
                .city("Dhaka")
                .country("Bangladesh")
                .isDefault(true)
                .defaultShipping(true)
                .build();
    }

    @Test
    void testCreateFirstAddressSetsDefaultFlags() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(sampleUser));
        when(addressRepository.countByUserIdAndDeletedFalse(eq(1L))).thenReturn(0L);
        when(addressMapper.toEntity(any(AddressRequest.class))).thenReturn(sampleAddress);
        when(addressRepository.save(any(Address.class))).thenReturn(sampleAddress);
        when(addressMapper.toResponse(any(Address.class))).thenReturn(sampleResponse);

        AddressResponse response = addressService.createAddress(1L, sampleRequest);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertTrue(response.isDefaultShipping());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testGetAddressByIdSuccess() {
        when(addressRepository.findByIdAndDeletedFalse(eq(10L))).thenReturn(Optional.of(sampleAddress));
        when(addressMapper.toResponse(eq(sampleAddress))).thenReturn(sampleResponse);

        AddressResponse response = addressService.getAddressById(10L, 1L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
    }

    @Test
    void testGetMyAddressesPaginated() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Address> addressPage = new PageImpl<>(Collections.singletonList(sampleAddress), pageable, 1);

        when(addressRepository.findByUserIdAndDeletedFalse(eq(1L), eq(pageable))).thenReturn(addressPage);
        when(addressMapper.toResponse(eq(sampleAddress))).thenReturn(sampleResponse);

        PageResponse<AddressResponse> pageResponse = addressService.getMyAddresses(1L, pageable);

        assertNotNull(pageResponse);
        assertEquals(1, pageResponse.getContent().size());
        assertEquals(10L, pageResponse.getContent().get(0).getId());
    }

    @Test
    void testSetDefaultShippingAddress() {
        when(addressRepository.findByIdAndDeletedFalse(eq(10L))).thenReturn(Optional.of(sampleAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(sampleAddress);
        when(addressMapper.toResponse(any(Address.class))).thenReturn(sampleResponse);

        AddressResponse response = addressService.setDefaultShippingAddress(10L, 1L);

        assertNotNull(response);
        verify(addressRepository, times(1)).unsetPreviousDefaultShipping(eq(1L));
        verify(addressRepository, times(1)).save(eq(sampleAddress));
    }

    @Test
    void testDeleteAddressSuccess() {
        when(addressRepository.findByIdAndDeletedFalse(eq(10L))).thenReturn(Optional.of(sampleAddress));

        addressService.deleteAddress(10L, 1L);

        assertTrue(sampleAddress.isDeleted());
        assertNotNull(sampleAddress.getDeletedAt());
        verify(addressRepository, times(1)).save(eq(sampleAddress));
    }
}
