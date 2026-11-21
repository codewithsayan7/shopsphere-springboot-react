package com.sayan.shopsphere;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerEmail;

    @Column(length = 10000)
    private String itemsJson;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    private String status = "PLACED";
    private Instant createdAt = Instant.now();

    public OrderRecord() {
    }

    public OrderRecord(
            String customerName,
            String customerEmail,
            String itemsJson,
            BigDecimal total
    ) {
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

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}