package com.example.productmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementApplication.class, args);
    }
    
    // // Temporary test - remove after verification
    // @Bean
    // CommandLineRunner test(ProductRepository repository) {
    //     return args -> {
    //         System.out.println("=== Testing Repository ===");
            
    //         // Count all products
    //         long count = repository.count();
    //         System.out.println("Total products: " + count);
            
    //         // Find all products
    //         List<Product> products = repository.findAll();
    //         products.forEach(System.out::println);
            
    //         // Find by category
    //         List<Product> electronics = repository.findByCategory("Electronics");
    //         System.out.println("\nElectronics: " + electronics.size());
            
    //         System.out.println("=== Test Complete ===");
    //     };
    // }
}

