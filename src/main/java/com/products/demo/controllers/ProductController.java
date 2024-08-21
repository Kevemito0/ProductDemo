package com.products.demo.controllers;

import com.products.demo.service.ProductService;
import com.products.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/Products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id)
    {
        return productService.getProductById(id);
    }
    @PostMapping
    public Product createProduct(@RequestBody Product product)
    {
        return productService.saveProduct(product);
    }
    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id)
    {
        productService.deleteProductById(id);
    }
}
