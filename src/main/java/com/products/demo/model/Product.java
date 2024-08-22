package com.products.demo.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;



@Entity (name = "product")
@Getter
@Setter
@Table (name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String category_code;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false,length = 5,unique = true)
    private String code;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String unit;

}
