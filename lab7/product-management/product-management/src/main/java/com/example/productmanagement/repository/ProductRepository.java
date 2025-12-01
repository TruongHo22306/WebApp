package com.example.productmanagement.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.productmanagement.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // TODO: Add method to find products by category
    List<Product> findByCategory(String category);
    
    // TODO: Add method to find products by category with sorting
    List<Product> findByCategory(String category, Sort sort);
    
    // TODO: Add method to find products by name containing keyword
    List<Product> findByNameContaining(String keyword);
    
    // TODO: Add method to find products by name containing keyword with pagination
    Page<Product> findByNameContaining(String keyword, Pageable pageable);
    
    // TODO: Add method to check if product code exists
    boolean existsByProductCode(String productCode);
    
    // Note: Basic CRUD methods are inherited from JpaRepository
    @Query("SELECT p FROM Product p WHERE " +
       "(:name IS NULL OR p.name LIKE %:name%) AND " +
       "(:category IS NULL OR p.category = :category) AND " +
       "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
       "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> searchProducts(@Param("name") String name,
                            @Param("category") String category,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
    List<String> findAllCategories();
    
    // TODO: Statistics queries
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category")
    long countByCategory(@Param("category") String category);
    
    @Query("SELECT SUM(p.price * p.quantity) FROM Product p")
    BigDecimal calculateTotalValue();
    
    @Query("SELECT AVG(p.price) FROM Product p")
    BigDecimal calculateAveragePrice();
    
    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold ORDER BY p.quantity ASC")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

}
