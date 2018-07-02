package com.ultimatesoftware.banking.authentication.service.config;

import com.ultimatesoftware.banking.authentication.service.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@Configuration
public class MongoDBConfig {

}

