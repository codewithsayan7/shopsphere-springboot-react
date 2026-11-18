package com.portfolio.shopsphere;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                || !(rawItems instanceof List<?> items)
                || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Name, email and at least one cart item are required."
            );
        }

        double orderTotal = 0;

        for (Object rawItem : items) {
            if (!(rawItem instanceof Map<?, ?> item)) {
                throw new IllegalArgumentException("Each cart item must be an object.");
            }

            Long productId;
            int quantity;

            try {
                productId = Long.valueOf(String.valueOf(item.get("productId")));
                quantity = Integer.parseInt(String.valueOf(item.get("quantity")));
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Each item needs a valid product ID and quantity.");
            }

            Product product = findProduct(productId);
            if (quantity < 1 || product.getStock() < quantity) {
                throw new IllegalArgumentException(
                        "There is not enough stock for " + product.getName() + "."
                );
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            orderTotal += product.getPrice() * quantity;
        }

        try {
            String itemsJson = objectMapper.writeValueAsString(items);
            return orderRepository.save(
                    new OrderRecord(customerName, customerEmail, itemsJson, orderTotal)
            );
        } catch (Exception exception) {
            throw new IllegalStateException("The order could not be created.", exception);
        }
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
    }
}
