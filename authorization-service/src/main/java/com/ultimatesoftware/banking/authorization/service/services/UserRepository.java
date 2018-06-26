package com.ultimatesoftware.banking.authorization.service.services;

import com.ultimatesoftware.banking.authorization.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    User findByUserName(String userName);

}
