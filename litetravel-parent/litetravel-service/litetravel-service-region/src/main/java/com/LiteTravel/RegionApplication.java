package com.LiteTravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.LiteTravel.mapper"})
public class RegionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegionApplication.class, args);
    }
}
