package com.ultimatesoftware.banking.authorization.service.config;

import com.ultimatesoftware.banking.authorization.service.services.ServiceUserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = ServiceUserRepository.class)
@Configuration
public class MongoDBConfig {


}

