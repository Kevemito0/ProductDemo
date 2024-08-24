package com.products.demo.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;


@Getter
@Setter
public class Category {

    private Long id;

    private String productCode;

    private String teraziCode;

    private String kasaCode;

}