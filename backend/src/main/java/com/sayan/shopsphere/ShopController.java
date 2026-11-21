package com.sayan.shopsphere;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShopController {

    private final ShopService service;

    public ShopController(ShopService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public List<Product> products(@RequestParam(required = false) String q) {
        return service.all(q);
    }

    @PostMapping("/products")
    public Product create(@Valid @RequestBody Product product) {
        return service.save(product);
    }

    @PutMapping("/products/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return service.update(id, product);
    }

    @DeleteMapping("/products/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/orders")
    public List<OrderRecord> orders() {
        return service.orders();
    }

    @PostMapping("/orders")
    public OrderRecord place(@RequestBody Map<String, Object> orderRequest) {
        return service.place(orderRequest);
    }
}
