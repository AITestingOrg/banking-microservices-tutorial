package com.ultimatesoftware.banking.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@EnableDiscoveryClient
@SpringBootApplication
public class TransactionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionsApplication.class, args);
    }
}
