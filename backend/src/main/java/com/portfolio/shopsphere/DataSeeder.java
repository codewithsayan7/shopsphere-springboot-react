package com.portfolio.shopsphere;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
class DataSeeder {

    @Bean
    CommandLineRunner seed(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(product(
                        "Everyday Backpack",
                        "A practical daily backpack with a clean minimal profile.",
                        "Accessories",
                        "1299.00",
                        20
                ));
                repository.save(product(
                        "Ceramic Coffee Mug",
                        "A sturdy ceramic mug for your daily coffee ritual.",
                        "Home",
                        "349.00",
                        35
                ));
                repository.save(product(
                        "Desk Organizer",
                        "Keep your workspace tidy with this compact organizer.",
                        "Workspace",
                        "599.00",
                        15
                ));
            }
        };
    }

    private Product product(
            String name,
            String description,
            String category,
            String price,
            int stock
    ) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(new BigDecimal(price));
        product.setStock(stock);
        return product;
    }
}