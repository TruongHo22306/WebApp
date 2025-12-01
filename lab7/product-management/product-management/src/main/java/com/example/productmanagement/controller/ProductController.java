package com.example.productmanagement.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.service.ProductService;

import jakarta.validation.Valid;

// TODO: Add @Controller annotation
// TODO: Add @RequestMapping("/products")
@Controller
@RequestMapping("/products")
public class ProductController {
    
    // TODO: Inject ProductService
    private final ProductService productService;
    
    // TODO: Create constructor with @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    // TODO: List all products - GET /products
    @GetMapping
    public String listProducts(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir,
        Model model) {
        
        // 1. Get all products from service (filtered by category and sorted)
        List<Product> products;
        
        // Create Sort object if sortBy is specified
        Sort sort = null;
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDir.equals("asc") ? 
                Sort.by(sortBy).ascending() : 
                Sort.by(sortBy).descending();
        }
        
        // Get products with filtering and sorting
        if (category != null && !category.isEmpty()) {
            products = sort != null ? 
                productService.getProductsByCategory(category, sort) : 
                productService.getProductsByCategory(category);
        } else {
            products = sort != null ? 
                productService.getAllProducts(sort) : 
                productService.getAllProducts();
        }
        
        // 2. Get all categories for filter dropdown
        List<String> categories = productService.getAllCategories();
        
        // 3. Add to model
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        // 4. Return "product-list"
        return "product-list";
    }
    
    // TODO: Show new product form - GET /products/new
    @GetMapping("/new")
    public String showNewForm(Model model) {
        // 1. Create empty Product object
        Product product = new Product();
        // 2. Add to model
        model.addAttribute("product", product);
        // 3. Return "product-form"
        return "product-form";
    }
    
    // TODO: Show edit form - GET /products/edit/{id}
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        // 1. Get product by id from service
        Optional<Product> optionalProduct = productService.getProductById(id);
        // 2. If found, add to model and return "product-form"
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            return "product-form";
        } else {
            // 3. If not found, add error message and redirect to list
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
            return "redirect:/products";
        }
    }
    
    // TODO: Save product - POST /products/save
    @PostMapping("/save")
    public String saveProduct(
        @Valid @ModelAttribute("product") Product product,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes) {
        
        // 1. Check for validation errors
        if (result.hasErrors()) {
            return "product-form";
        }
        
        // 2. Save product using service with error handling
        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        
        // 3. Redirect to list
        return "redirect:/products";
    }
    
    // TODO: Delete product - GET /products/delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // 1. Delete product using service
        productService.deleteProduct(id);
        // 2. Add success message
        redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully");
        // 3. Redirect to list
        return "redirect:/products";
    }
    
    // TODO: Search products - GET /products/search
    @GetMapping("/search")
    public String searchProducts(
        @RequestParam("keyword") String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model) {
        
        // 1. Create Pageable object
        Pageable pageable = PageRequest.of(page, size);
        
        // 2. Search products with pagination
        Page<Product> productPage = productService.searchProducts(keyword, pageable);
        
        // 3. Add results to model
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        
        // 4. Get all categories for filter dropdown
        List<String> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        
        // 5. Return "product-list"
        return "product-list";
    }

    @GetMapping("/advanced-search")
    public String advancedSearch(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        Model model) {
        // Implementation
        List<Product> products = productService.searchProducts(name, category, minPrice, maxPrice);
        model.addAttribute("products", products);
        return "product-list";
    }  


}
