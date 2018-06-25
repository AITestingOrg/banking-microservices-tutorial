package com.ultimatesoftware.banking.authorization.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class AuthorizationService {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationService.class, args);
    }
}
