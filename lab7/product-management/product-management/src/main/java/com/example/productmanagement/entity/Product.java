package com.example.productmanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {
    
    // TODO: Add @Id annotation
    // TODO: Add @GeneratedValue with IDENTITY strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // TODO: Add @Column with unique=true, nullable=false
    @Column(name = "product_code", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Product code is required")
    @Size(min = 3, max = 20, message = "Product code must be 3-20 characters")
    @Pattern(regexp = "^P\\d{3,}$", message = "Product code must start with P followed by numbers")
    private String productCode;
    
    // TODO: Add @Column with nullable=false
    @Column(nullable = false, length = 200)
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Name must be 3-100 characters")
    private String name;
    
    // TODO: Add @Column with precision and scale for decimal
    @Column(precision = 10, scale = 2)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price is too high")
    private BigDecimal price;
    
    // TODO: Add @Column for quantity
    @Column
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    // TODO: Add @Column for category
    @Column(length = 100)
    @NotBlank(message = "Category is required")
    private String category;
    
    // TODO: Add @Column with TEXT type
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // TODO: Add @Column for created_at with updatable=false
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // TODO: Add no-arg constructor
    public Product() {
    }
    
    // TODO: Add parameterized constructor (without id and createdAt)
    public Product(String productCode, String name, BigDecimal price, Integer quantity, String category, String description) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
    }
    
    // TODO: Add @PrePersist method to set createdAt
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    // TODO: Generate all getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
    // TODO: Add toString() method
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
