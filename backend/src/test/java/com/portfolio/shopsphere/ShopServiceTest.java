package com.portfolio.shopsphere;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShopServiceTest {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private ShopService service;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        service = new ShopService(productRepository, orderRepository, new ObjectMapper());
    }

    @Test
    void checkoutRejectsAnEmptyCart() {
        Map<String, Object> request = Map.of(
                "customerName", "Alex Doe",
                "customerEmail", "alex@example.com",
                "items", List.of()
        );

        assertThrows(IllegalArgumentException.class, () -> service.place(request));
        verifyNoInteractions(productRepository, orderRepository);
    }

    @Test
    void checkoutRejectsMissingCustomerDetails() {
        Map<String, Object> request = Map.of("items", List.of(Map.of(
                "productId", 1,
                "quantity", 1
        )));

        assertThrows(IllegalArgumentException.class, () -> service.place(request));
        verifyNoInteractions(productRepository, orderRepository);
    }

    @Test
    void checkoutRejectsAnInvalidEmail() {
        Map<String, Object> request = Map.of(
                "customerName", "Alex Doe",
                "customerEmail", "not-an-email",
                "items", List.of(Map.of("productId", 1, "quantity", 1))
        );

        assertThrows(IllegalArgumentException.class, () -> service.place(request));
        verifyNoInteractions(productRepository, orderRepository);
    }

    @Test
    void checkoutReducesStockAndUsesExactDecimalMoney() {
        Product product = new Product();
        product.setName("Desk Lamp");
        product.setPrice(new BigDecimal("19.99"));
        product.setStock(5);

        when(productRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(OrderRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> request = Map.of(
                "customerName", "Alex Doe",
                "customerEmail", "alex@example.com",
                "items", List.of(Map.of("productId", 7, "quantity", 2))
        );

        OrderRecord order = service.place(request);

        assertEquals(3, product.getStock());
        assertEquals(new BigDecimal("39.98"), order.getTotal());
        verify(productRepository).findByIdForUpdate(7L);
        verify(orderRepository).save(any(OrderRecord.class));
    }

    @Test
    void checkoutRejectsOrdersLargerThanAvailableStock() {
        Product product = new Product();
        product.setName("Desk Lamp");
        product.setPrice(new BigDecimal("19.99"));
        product.setStock(1);

        when(productRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(product));

        Map<String, Object> request = Map.of(
                "customerName", "Alex Doe",
                "customerEmail", "alex@example.com",
                "items", List.of(Map.of("productId", 7, "quantity", 2))
        );

        assertThrows(IllegalArgumentException.class, () -> service.place(request));
        verify(orderRepository, never()).save(any());
    }
}