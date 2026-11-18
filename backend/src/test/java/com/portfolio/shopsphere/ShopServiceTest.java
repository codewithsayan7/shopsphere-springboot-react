package com.portfolio.shopsphere;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ShopServiceTest {

    private ShopService service;

    @BeforeEach
    void setUp() {
        service = new ShopService(
                mock(ProductRepository.class),
                mock(OrderRepository.class),
                new ObjectMapper()
        );
    }

    @Test
    void checkoutRejectsAnEmptyCart() {
        Map<String, Object> request = Map.of(
                "customerName", "Alex Doe",
                "customerEmail", "alex@example.com",
                "items", java.util.List.of()
        );

        assertThrows(IllegalArgumentException.class, () -> service.place(request));
    }

    @Test
    void checkoutRejectsMissingCustomerDetails() {
        Map<String, Object> request = Map.of("items", java.util.List.of(Map.of(
                "productId", 1,
                "quantity", 1
        )));

        assertThrows(IllegalArgumentException.class, () -> service.place(request));
    }
}
