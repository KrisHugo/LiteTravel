package com.LiteTravel.Search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.LiteTravel.Search", "com.framework"})
@EnableFeignClients(basePackages = {"com.LiteTravel.Hotel.feign"})
@EnableElasticsearchRepositories(basePackages = "com.LiteTravel.Search.dao")
public class SearchApplication {

    public static void main(String[] args) {
        /* 此处启动与SpringBoot底层的netty可能有一定冲突, 因此进行配置, 若未来无报错, 可删除 */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(SearchApplication.class, args);
    }
}
