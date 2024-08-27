package com.products.demo.controllers;

import com.products.demo.model.Category;
import com.products.demo.model.dto.CategoryDTO;
import com.products.demo.response.ProductResponse;
import com.products.demo.service.ProductService;
import com.products.demo.model.Product;
import com.products.demo.webtest.GreetingController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/Products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id)
    {
        return productService.getProductById(id);
    }

    @GetMapping("/code/{code}")
    public Mono<Product> getProductByCode(@PathVariable String code) {
        return productService.getProductByCode(code);
    }

    @GetMapping("/external/{id}")
    public Mono<Product> getExternalProduct(@PathVariable String id){
        return productService.getProductFromOtherService(id);
    }

    @GetMapping("/external")
    public Mono<List<Product>>getAllProductsExternal(){
        return productService.getAllProductsExternal();
    }

    @PutMapping("/{id}/update-barcode")
    public Mono<Product> updateProductBarcode(@PathVariable Long id) {
        return productService.updateProductBarcode(id);
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

    @PostMapping("/externalbarcode")
    public Mono<Product> createProductAndBarcode(@RequestBody Product product){
        return productService.createProductAndBarcode(product);
    }

    @PostMapping("/externalPost")
    public Mono<Product> externalCreateProduct(@RequestBody Product product)
    {
        return productService.externalCreateProduct(product);
    }
    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id)
    {
        productService.deleteProductById(id);
    }

    @GetMapping("/external/category")
    public Mono<List<Category>>getAllCategory(){
        return productService.getAllCategory();
    }
    @GetMapping("/external/kasa/{kasa}")
    public Mono<Category>getCategoryBykasa(@PathVariable String kasa){
        return productService.getCategoryBykasa(kasa);
    }
    @GetMapping("/external/terazi/{terazi}")
    public Mono<Category>getCategoryByTerazi(@PathVariable String terazi){

    /*
        List<CategoryDTO> categoryDTO = new ArrayList<>();
        categoryDTO.stream().map(CategoryDTO::getId);
        */
        return productService.getCategoryByterazi(terazi);
    }

    @GetMapping("/external/product/{product}")
    public Mono<Category>getCategoryByproduct(@PathVariable String product){
        return productService.getCategoryByproduct(product);
    }
}
