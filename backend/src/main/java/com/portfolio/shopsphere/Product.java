package com.portfolio.shopsphere;
import jakarta.persistence.*;import jakarta.validation.constraints.*;
@Entity @Table(name="products")
public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank private String name; @NotBlank @Column(length=2000) private String description;
 @NotBlank private String category; @DecimalMin("0.01") private double price;
 @Min(0) private int stock; private String imageUrl;
 public Product(){}
 public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public String getCategory(){return category;} public void setCategory(String v){category=v;}
 public double getPrice(){return price;} public void setPrice(double v){price=v;}
 public int getStock(){return stock;} public void setStock(int v){stock=v;}
 public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
}
