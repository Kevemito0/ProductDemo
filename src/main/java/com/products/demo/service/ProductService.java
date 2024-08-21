package com.products.demo.service;

import com.products.demo.repo.ProductRepo;
import com.products.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public Optional<Product> getProductById(Long id){
        return productRepo.findById(id);
    }

    public Product saveProduct(Product product){
        return productRepo.save(product);
    }

    public void deleteProductById(Long id){
        productRepo.deleteById(id);
    }
}
