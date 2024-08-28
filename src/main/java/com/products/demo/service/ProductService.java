package com.products.demo.service;

import com.products.demo.model.CategoryDemo;
import com.products.demo.repo.ProductRepo;
import com.products.demo.model.Product;
import com.products.demo.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Random;
import java.util.Optional;
@RestController
@RequestMapping("/api/Products")
@RequiredArgsConstructor
@Service
public class ProductService {
    private final RestClient restClient;
    String baseUrl = "http://localhost:8080/api/Categories";
    String CategoryBaseUrl = "http://localhost:8082/api/category/";
    @Autowired
    private ProductRepo productRepo;

    private Random random = new Random();


    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
    public List<Category> getAllCategories(){
        return restClient.get().uri(baseUrl).retrieve().body(List.class);
    }
    public Optional<Product> getProductBycode(String code){
        return productRepo.findAll().stream().filter(product -> product.getCode().equals(code)).findFirst();
    }
    public Category getExternalCategory(Long id){
        ResponseEntity<Category> result = restClient.get().uri(baseUrl + "/{id}",id).retrieve().toEntity(Category.class);
        System.out.println(result.getHeaders());
        return result.getBody();
        //return restClient.get().uri(baseUrl + "/{id}",id).retrieve().body(Category.class);
    }
    public CategoryDemo getExternalCategoryDemo(Long id){
        return restClient.get().uri(CategoryBaseUrl + "/{id}",id).retrieve().body(CategoryDemo.class);
    }
    public Optional<Product> getProductById(Long id){
        return productRepo.findById(id);
    }

    public Product saveProduct(Product product){
        return productRepo.save(product);
    }

    public void deleteProductById(Long id){
        restClient.delete().uri(baseUrl + "/deletebarcode/{barcode}",getProductById(id).get().getBarcode()).retrieve().toEntity(Product.class);
        productRepo.deleteById(id);

    }
    /*
    public Mono<Product> getProductFromOtherService(String id){
        return RestClient.get().uri("/api/service1/Products/{id}",id).retrieve().bodyToMono(Product.class);
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
    */
    public Product createProductAndBarcode(Product product){
        Optional<Product> existingProduct = getProductBycode(product.getCode());
        if(existingProduct.isPresent()){
            System.out.println("Products exists: " + product.getName() + ", " + product.getCode());
            return existingProduct.get();
        }
        else{
            System.out.println(product.getCode());
            product.setBarcode(restClient.post().uri(baseUrl + "/create").
                    contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(Category.class)
                    .getProductCode());
            //product.setCategoryCode(restClient.get().uri(CategoryBaseUrl + "/name/{id}",product.getCategoryCode()));
            return productRepo.save(product);
        }
        /*
        return webClient.post().uri("/api/Categories/barcodeGenerator/{category_code}",catcode).bodyValue(savedProduct.getCategoryCode()).retrieve().bodyToMono(Category.class)
                .flatMap((barcodes -> {
                    String selectedBarcode = barcodes.getProductCode();
                    savedProduct.setCode(selectedBarcode);
                    productRepo.save(savedProduct);
                }));
                */
    }
    public Product changeProductBarcode(Long id,int place){
        Product product = getProductById(id).get();
        String productCode = product.getBarcode();
        String prodUnit = String.valueOf(product.getUnit());

        if(product.getCategoryCode() == 3) //checks if the products category is balik or not
        {
            if(prodUnit == "Kilogram") {
                if (place == 0)
                    product.setBarcode(restClient.get().uri(baseUrl + "/barcode/{barcode}", productCode).retrieve().body(Category.class).getScaleCode());
                else
                    product.setBarcode(restClient.get().uri(baseUrl + "/barcode/{barcode}", productCode).retrieve().body(Category.class).getProductCode());
            }
            if(prodUnit == "Adet"){ //also for the balık
                product.setBarcode(restClient.get().uri(baseUrl + "/barcode/{barcode}",productCode).retrieve().body(Category.class).getCashregCode());
            }
        }
        else if(product.getCategoryCode() == 1){
            if(place == 0)
                product.setBarcode(restClient.get().uri(baseUrl + "/barcode/{barcode}", productCode).retrieve().body(Category.class).getCashregCode());
            else
                product.setBarcode(restClient.get().uri(baseUrl + "/barcode/{barcode}", productCode).retrieve().body(Category.class).getProductCode());
        }
        else if(product.getCategoryCode() == 6){
            product.setBarcode(restClient.get().uri(baseUrl + "/barcode/{barcode}", productCode).retrieve().body(Category.class).getScaleCode());
        }
        else
            product.setBarcode(restClient.get().uri(baseUrl + "/barcode/{barcode}", productCode).retrieve().body(Category.class).getProductCode());

        return productRepo.save(product);
    }
    /*
    //generating only product barcode
    private String generateRandomBarcode(String code, int length)
    {
        int barcodelength = length;
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
            Optional<Product> productOptional = productRepo.findById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                return getCategoryBykasa("1234")
                        .flatMap(category -> {
                            String newBarcode = category.getKasaCode();
                            product.setCode(newBarcode);
                            productRepo.save(product);
                            return Mono.just(product);
                        });
            } else {
                return Mono.empty();
            }
        });
    }

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


