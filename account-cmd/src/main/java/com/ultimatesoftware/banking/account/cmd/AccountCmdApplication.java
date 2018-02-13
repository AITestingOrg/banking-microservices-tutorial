package com.ultimatesoftware.banking.account.cmd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableEurekaClient
public class AccountCmdApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountCmdApplication.class, args);
	}
}
