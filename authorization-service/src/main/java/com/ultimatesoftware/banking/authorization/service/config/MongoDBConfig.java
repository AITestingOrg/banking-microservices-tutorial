package com.ultimatesoftware.banking.authorization.service.config;

import com.ultimatesoftware.banking.authorization.service.services.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@Configuration
public class MongoDBConfig {


}

