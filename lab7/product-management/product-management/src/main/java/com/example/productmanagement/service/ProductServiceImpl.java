package com.example.productmanagement.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.repository.ProductRepository;

// TODO: Add @Service annotation
// TODO: Add @Transactional annotation
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    // TODO: Inject ProductRepository using constructor injection
    private final ProductRepository productRepository;
    
    // TODO: Create constructor with @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public List<Product> getAllProducts() {
        // TODO: Return all products from repository
        return productRepository.findAll();
    }
    
    @Override
    public List<Product> getAllProducts(Sort sort) {
        // TODO: Return all products with sorting
        return productRepository.findAll(sort);
    }
    
    @Override
    public Optional<Product> getProductById(Long id) {
        // TODO: Return product by id from repository
        return productRepository.findById(id);
    }
    
    @Override
    public Product saveProduct(Product product) {
        // TODO: Save product to repository
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(Long id) {
        // TODO: Delete product from repository
        productRepository.deleteById(id);
    }
    
    @Override
    public List<Product> searchProducts(String keyword) {
        // TODO: Search products using repository method
        return productRepository.findByNameContaining(keyword);
    }
    
    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        // TODO: Search products with pagination
        return productRepository.findByNameContaining(keyword, pageable);
    }
    
    @Override
    public List<Product> getProductsByCategory(String category) {
        // TODO: Get products by category from repository
        return productRepository.findByCategory(category);
    }
    
    @Override
    public List<Product> getProductsByCategory(String category, Sort sort) {
        // TODO: Get products by category with sorting
        return productRepository.findByCategory(category, sort);
    }

    @Override
    public List<Product> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }
    
    @Override
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }
    
    @Override
    public long countByCategory(String category) {
        return productRepository.countByCategory(category);
    }
    
    @Override
    public BigDecimal calculateTotalValue() {
        BigDecimal total = productRepository.calculateTotalValue();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal calculateAveragePrice() {
        BigDecimal avg = productRepository.calculateAveragePrice();
        return avg != null ? avg : BigDecimal.ZERO;
    }
    
    @Override
    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }
    
}
