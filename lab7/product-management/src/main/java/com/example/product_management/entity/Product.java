package com.example.product_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_code", unique = true, nullable = false, length = 20)
    private String productCode;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(length = 50)
    private String category;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public Product() {
    }
    
    public Product(String productCode, String name, BigDecimal price, Integer quantity, String category, String description) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
    }
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // getters + setters ...
}
