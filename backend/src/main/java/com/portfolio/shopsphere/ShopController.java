package com.portfolio.shopsphere;
import org.springframework.web.bind.annotation.*;import jakarta.validation.Valid;import java.util.*;
@RestController @RequestMapping("/api")
public class ShopController {
 private final ShopService service;public ShopController(ShopService s){service=s;}
 @GetMapping("/products") public List<Product> products(@RequestParam(required=false)String q){return service.all(q);}
 @PostMapping("/products") public Product create(@Valid @RequestBody Product p){return service.save(p);}
 @PutMapping("/products/{id}") public Product update(@PathVariable Long id,@Valid @RequestBody Product p){return service.update(id,p);}
 @DeleteMapping("/products/{id}") public void delete(@PathVariable Long id){service.delete(id);}
 @GetMapping("/orders") public List<OrderRecord> orders(){return service.orders();}
 @PostMapping("/orders") public OrderRecord place(@RequestBody Map<String,Object> body){return service.place(body);}
}
