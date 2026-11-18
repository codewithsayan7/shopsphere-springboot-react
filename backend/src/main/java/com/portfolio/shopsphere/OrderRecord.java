package com.portfolio.shopsphere;
import jakarta.persistence.*;import java.time.Instant;
@Entity @Table(name="orders")
public class OrderRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String customerName; private String customerEmail; private String itemsJson; private double total; private String status="PLACED"; private Instant createdAt=Instant.now();
 public OrderRecord(){}
 public OrderRecord(String n,String e,String j,double t){customerName=n;customerEmail=e;itemsJson=j;total=t;}
 public Long getId(){return id;} public String getCustomerName(){return customerName;} public String getCustomerEmail(){return customerEmail;}
 public String getItemsJson(){return itemsJson;} public double getTotal(){return total;} public String getStatus(){return status;} public Instant getCreatedAt(){return createdAt;}
}
