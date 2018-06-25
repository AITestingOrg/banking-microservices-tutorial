package com.ultimatesoftware.banking.authorization.service.config;

import com.ultimatesoftware.banking.authorization.service.User.ServiceUserRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.context.annotation.Configuration;

@EnableMongoRepositories(basePackageClasses = ServiceUserRepository.class)
@Configuration
public class MongoDBConfig {


}

