package com.ultimatesoftware.banking.customers.cmd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CustomerCmdApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerCmdApplication.class, args);
	}
}
