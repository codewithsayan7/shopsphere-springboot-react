package com.portfolio.shopsphere;
import org.springframework.boot.CommandLineRunner;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;
@Configuration class DataSeeder {
 @Bean CommandLineRunner seed(ProductRepository repo){return args->{if(repo.count()==0){
  Product a=new Product();a.setName("Everyday Backpack");a.setDescription("A practical daily backpack with a clean minimal profile.");a.setCategory("Accessories");a.setPrice(1299);a.setStock(20);repo.save(a);
  Product b=new Product();b.setName("Ceramic Coffee Mug");b.setDescription("A sturdy ceramic mug for your daily coffee ritual.");b.setCategory("Home");b.setPrice(349);b.setStock(35);repo.save(b);
  Product c=new Product();c.setName("Desk Organizer");c.setDescription("Keep your workspace tidy with this compact organizer.");c.setCategory("Workspace");c.setPrice(599);c.setStock(15);repo.save(c);
 }};}
}
