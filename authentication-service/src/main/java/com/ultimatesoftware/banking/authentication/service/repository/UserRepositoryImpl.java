package com.ultimatesoftware.banking.authentication.service.repository;

import com.ultimatesoftware.banking.authentication.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongo;

    public User findByUserName(String userName) {
        Query getUser = new Query(Criteria.where("userName").is(userName));
        return mongo.findOne(getUser, User.class);
    }
}
