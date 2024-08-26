package com.products.demo.model.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CategoryDTO {

    private List<Long> id;
    private String name;
    private String description;
}
