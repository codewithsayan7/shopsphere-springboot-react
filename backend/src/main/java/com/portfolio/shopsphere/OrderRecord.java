package com.portfolio.shopsphere;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerEmail;
    private String itemsJson;
    private double total;
    private String status = "PLACED";
    private Instant createdAt = Instant.now();

    public OrderRecord() {
    }

    public OrderRecord(String customerName, String customerEmail, String itemsJson, double total) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.itemsJson = itemsJson;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getItemsJson() {
        return itemsJson;
    }

    public double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
