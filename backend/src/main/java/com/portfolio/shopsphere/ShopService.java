package com.portfolio.shopsphere;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ShopService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public ShopService(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            ObjectMapper objectMapper
    ) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    public List<Product> all(String query) {
        if (query == null || query.isBlank()) {
            return productRepository.findAll();
        }

        String searchTerm = query.trim();
        return productRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                searchTerm,
                searchTerm
        );
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product update(Long id, Product updatedProduct) {
        Product existingProduct = findProduct(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        return productRepository.save(existingProduct);
    }

    public void delete(Long id) {
        productRepository.delete(findProduct(id));
    }

    public List<OrderRecord> orders() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderRecord place(Map<String, Object> orderRequest) {
        String customerName = String.valueOf(
                orderRequest.getOrDefault("customerName", "")
        ).trim();
        String customerEmail = String.valueOf(
                orderRequest.getOrDefault("customerEmail", "")
        ).trim();

        Object rawItems = orderRequest.get("items");
        if (customerName.isEmpty()
                || customerEmail.isEmpty()
                || !customerEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || !(rawItems instanceof List<?> items)
                || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Enter a valid name and email, and add at least one cart item."
            );
        }

        BigDecimal orderTotal = BigDecimal.ZERO;

        for (Object rawItem : items) {
            if (!(rawItem instanceof Map<?, ?> item)) {
                throw new IllegalArgumentException("Each cart item must be an object.");
            }

            Long productId;
            int quantity;
            try {
                productId = Long.valueOf(String.valueOf(item.get("productId")));
                quantity = Integer.parseInt(String.valueOf(item.get("quantity")));
            } catch (RuntimeException exception) {
                throw new IllegalArgumentException(
                        "Each item needs a valid product ID and quantity."
                );
            }

            if (quantity < 1) {
                throw new IllegalArgumentException("Item quantity must be at least one.");
            }

            Product product = productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));

            if (product.getStock() < quantity) {
                throw new IllegalArgumentException(
                        "There is not enough stock for " + product.getName() + "."
                );
            }

            product.setStock(product.getStock() - quantity);
            orderTotal = orderTotal.add(
                    product.getPrice().multiply(BigDecimal.valueOf(quantity))
            );
        }

        try {
            String itemsJson = objectMapper.writeValueAsString(items);
            return orderRepository.save(
                    new OrderRecord(customerName, customerEmail, itemsJson, orderTotal)
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("The order could not be created.", exception);
        }
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
    }
}