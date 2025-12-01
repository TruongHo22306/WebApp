package com.example.productmanagement.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.productmanagement.entity.Product;

// TODO: This is the service interface
public interface ProductService {
    
    // TODO: Method to get all products
    List<Product> getAllProducts();
    
    // TODO: Method to get all products with sorting
    List<Product> getAllProducts(Sort sort);
    
    // TODO: Method to get product by id
    Optional<Product> getProductById(Long id);
    
    // TODO: Method to save/update product
    Product saveProduct(Product product);
    
    // TODO: Method to delete product
    void deleteProduct(Long id);
    
    // TODO: Method to search products by keyword
    List<Product> searchProducts(String keyword);
    
    // TODO: Method to search products by keyword with pagination
    Page<Product> searchProducts(String keyword, Pageable pageable);
    
    // TODO: Method to get products by category
    List<Product> getProductsByCategory(String category);
    
    // TODO: Method to get products by category with sorting
    List<Product> getProductsByCategory(String category, Sort sort);

    List<Product> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice);
    
    // TODO: Method to get all unique categories
    List<String> getAllCategories();
    
    // TODO: Statistics methods
    long countByCategory(String category);
    BigDecimal calculateTotalValue();
    BigDecimal calculateAveragePrice();
    List<Product> findLowStockProducts(int threshold);
}
