package com.LiteTravel.Region;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.LiteTravel.Region.mapper"})
@ComponentScan(basePackages = {"com.LiteTravel.Region", "com.framework"})
/* answer ComponentScan注解是用来标识Spring扫描的包, 不标注默认是该类下的所有子包, 但由于要使用到共用类的ExceptionHandler, 因此需要使用这个注解 */
public class RegionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegionApplication.class, args);
    }
}
