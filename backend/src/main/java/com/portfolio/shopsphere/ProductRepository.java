package com.portfolio.shopsphere;
import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface ProductRepository extends JpaRepository<Product,Long>{List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String n,String c);}
