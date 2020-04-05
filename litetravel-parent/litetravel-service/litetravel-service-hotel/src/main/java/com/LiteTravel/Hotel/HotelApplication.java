package com.LiteTravel.Hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.LiteTravel.Hotel.mapper"})
@ComponentScan(basePackages = {"com.LiteTravel.Hotel", "com.framework"})
@EnableFeignClients(basePackages = {"com.LiteTravel.Region.feign"})
public class HotelApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelApplication.class, args);
    }

}
