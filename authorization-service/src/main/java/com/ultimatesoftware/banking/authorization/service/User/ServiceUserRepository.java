package com.ultimatesoftware.banking.authorization.service.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceUserRepository extends MongoRepository<ServiceUser, Long> {
    ServiceUser findByUsername(String username);

}
