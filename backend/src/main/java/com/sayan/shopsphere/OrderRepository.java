package com.sayan.shopsphere;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<OrderRecord,Long>{}
