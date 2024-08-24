package com.products.demo.service;

import com.products.demo.repo.ProductRepo;
import com.products.demo.model.Product;
import com.products.demo.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Random;
import java.util.Optional;

@Service
public class ProductService {
    private final WebClient webClient;
    @Autowired
    private ProductRepo productRepo;

    private Random random = new Random();


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
    public Mono<List<Product>> getAllProductsExternal(){
        return webClient.get().uri("/api/service1/Products").retrieve().bodyToMono(new ParameterizedTypeReference<List<Product>>() {});
    }
    public Mono<Product> externalCreateProduct(Product product){
        return webClient.post().uri("/api/service1/Products").bodyValue(product).retrieve().bodyToMono(Product.class);
    }
    public Mono<Product> getProductByCode(String code){
        return webClient.get().uri("/api/service1/Products/code/{code}",code).retrieve().bodyToMono(Product.class);
    }

    public Mono<List<Category>> getAllCategory(){
        return webClient.get().uri("/api/Categories").retrieve().bodyToMono(new ParameterizedTypeReference<List<Category>>() {});
    }

    public Mono<Category> getCategoryBykasa(String kasa){
        return webClient.get().uri("/api/Categories/kasa/{kasa}",kasa).retrieve().bodyToMono(Category.class);
    }

    public Mono<Category> getCategoryByterazi(String terazi){
        return webClient.get().uri("/api/Categories/terazi/{terazi}",terazi).retrieve().bodyToMono(Category.class);
    }

    public Mono<Category> getCategoryByproduct(String product){
        return webClient.get().uri("/api/Categories/product/{product}",product).retrieve().bodyToMono(Category.class);
    }
    //generating only product barcode
    private String generateRandomBarcode(String code)
    {
        int barcodelength = 5;
        String barcodeLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder barcode = new StringBuilder (code);
        for(int i = 2; i < barcodelength; i++)
        {
            int index = random.nextInt(barcodeLetter.length());
            barcode.append(barcodeLetter.charAt(index));
        }
        return barcode.toString();
    }
    public Mono<Product> updateProductBarcode(Long id) {
        return Mono.defer(() -> {
            Optional<Product> productOptional = productRepo.findById(id);  // Blocking call
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                return getCategoryBykasa("1234")
                        .flatMap(category -> {
                            String newBarcode = category.getKasaCode();
                            product.setCode(newBarcode);
                            productRepo.save(product);  // Blocking call
                            return Mono.just(product);  // Wrap in Mono
                        });
            } else {
                return Mono.empty();
            }
        });
    }
    /*
    public Product updateProductBarcode(Long id){
        Optional<Product> productOptional = productRepo.findById(id);
        String productBaseCode;
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            productBaseCode = product.getCategory_code();
            String newBarcode = generateRandomBarcode(productBaseCode);
            product.setCode(newBarcode);
            productRepo.save(product);
            System.out.println("Updated barcode for product with ID: " + id + " to " + newBarcode);
            return product;
        }
        else {
            System.out.println("Product with ID: " + id + " not found");
            return null;
        }
    } */
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

