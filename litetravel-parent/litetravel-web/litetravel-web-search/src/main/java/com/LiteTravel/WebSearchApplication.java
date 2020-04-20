package com.LiteTravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.LiteTravel.Search.Feign"})
public class WebSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSearchApplication.class, args);
    }
}
