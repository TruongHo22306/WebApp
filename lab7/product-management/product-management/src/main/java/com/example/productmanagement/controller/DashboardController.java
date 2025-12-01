package com.example.productmanagement.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.service.ProductService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    private final ProductService productService;
    
    public DashboardController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public String showDashboard(Model model) {
        // Total products count
        long totalProducts = productService.getAllProducts().size();
        model.addAttribute("totalProducts", totalProducts);
        
        // Products by category
        List<String> categories = productService.getAllCategories();
        Map<String, Long> categoryStats = new HashMap<>();
        for (String category : categories) {
            long count = productService.countByCategory(category);
            categoryStats.put(category, count);
        }
        model.addAttribute("categoryStats", categoryStats);
        
        // Total inventory value
        BigDecimal totalValue = productService.calculateTotalValue();
        model.addAttribute("totalValue", totalValue);
        
        // Average product price
        BigDecimal averagePrice = productService.calculateAveragePrice();
        model.addAttribute("averagePrice", averagePrice);
        
        // Low stock alerts (quantity < 10)
        List<Product> lowStockProducts = productService.findLowStockProducts(10);
        model.addAttribute("lowStockProducts", lowStockProducts);
        
        // Recent products (last 5 added) - ordered by createdAt descending
        List<Product> recentProducts = productService.getAllProducts(
            Sort.by(Sort.Direction.DESC, "createdAt")
        );
        // Get only first 5
        if (recentProducts.size() > 5) {
            recentProducts = recentProducts.subList(0, 5);
        }
        model.addAttribute("recentProducts", recentProducts);
        
        return "dashboard";
    }
}
