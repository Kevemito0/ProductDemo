package com.products.demo.controllers;

import com.products.demo.response.ProductResponse;
import com.products.demo.service.ProductService;
import com.products.demo.model.Product;
import com.products.demo.webtest.GreetingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/Products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private GreetingController greeting;
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/hello")
    public Map<String, String> sayHello() {return greeting.sayHello(); }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id)
    {
        return productService.getProductById(id);
    }

    @GetMapping("/{id}/external")
    public Mono<Product> getExternalProduct(@PathVariable String id){
        return productService.getProductFromOtherService(id);
    }

    /*
    @GetMapping("/other/{id}")
    private ResponseEntity<ProductResponse>getProductFromOtherService(@PathVariable("id") long id){
        ProductResponse product = productService.getProductByIdExternal(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
    */

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
