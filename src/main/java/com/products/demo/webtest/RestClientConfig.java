package com.products.demo.webtest;

import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestClientConfig {
    @Bean
    RestClient restClient() {
        return RestClient.builder().baseUrl("http://localhost:8081").build();
    }
}
