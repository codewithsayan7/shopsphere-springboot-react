package com.portfolio.shopsphere;
import org.springframework.stereotype.Service;import java.util.*;import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class ShopService {
 private final ProductRepository products;private final OrderRepository orders;private final ObjectMapper mapper=new ObjectMapper();
 public ShopService(ProductRepository p,OrderRepository o){products=p;orders=o;}
 public List<Product> all(String q){return q==null||q.isBlank()?products.findAll():products.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(q,q);}
 public Product save(Product p){return products.save(p);}
 public Product update(Long id,Product p){Product x=products.findById(id).orElseThrow(()->new NoSuchElementException("Product not found"));x.setName(p.getName());x.setDescription(p.getDescription());x.setCategory(p.getCategory());x.setPrice(p.getPrice());x.setStock(p.getStock());x.setImageUrl(p.getImageUrl());return products.save(x);}
 public void delete(Long id){products.delete(products.findById(id).orElseThrow(()->new NoSuchElementException("Product not found")));}
 public List<OrderRecord> orders(){return orders.findAll();}
 public OrderRecord place(Map<String,Object> body){
  String name=String.valueOf(body.getOrDefault("customerName","")).trim(),email=String.valueOf(body.getOrDefault("customerEmail","")).trim();
  Object raw=body.get("items");if(name.isEmpty()||email.isEmpty()||!(raw instanceof List<?> list)||list.isEmpty())throw new IllegalArgumentException("Name, email and at least one cart item are required");
  double total=0;
  for(Object o:list){Map<?,?> item=(Map<?,?>)o;Long id=Long.valueOf(String.valueOf(item.get("productId")));int qty=Integer.parseInt(String.valueOf(item.get("quantity")));Product p=products.findById(id).orElseThrow(()->new NoSuchElementException("Product not found: "+id));if(qty<1||p.getStock()<qty)throw new IllegalArgumentException("Insufficient stock for "+p.getName());p.setStock(p.getStock()-qty);products.save(p);total+=p.getPrice()*qty;}
  try{return orders.save(new OrderRecord(name,email,mapper.writeValueAsString(raw),total));}catch(Exception ex){throw new IllegalStateException("Could not create order",ex);}
 }
}
