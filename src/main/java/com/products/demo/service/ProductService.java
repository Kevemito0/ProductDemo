package com.products.demo.service;

import com.products.demo.repo.ProductRepo;
import com.products.demo.model.Product;
import com.products.demo.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
public class ProductService {
    private final WebClient webClient;
    @Autowired
    private ProductRepo productRepo;



    @Autowired
    public ProductService(WebClient.Builder webClientBuilder, ProductRepo productRepo) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        this.productRepo = productRepo;
    }

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

    public Mono<Product> getProductFromOtherService(String id){
        return webClient.get().uri("/api/service1/Products/{id}",id).retrieve().bodyToMono(Product.class);
    }
    /*
    public ProductResponse getProductByIdExternal(long id){
        Optional<Product> product = productRepo.findById(id);

        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.get().getId());
        productResponse.setName(product.get().getName());
        productResponse.setUnit(product.get().getUnit());
        productResponse.setCategory_code(product.get().getCategory_code());
        productResponse.setCode(product.get().getCode());
        productResponse.setBrand(product.get().getBrand());

        return productResponse;
    }
    */
}
