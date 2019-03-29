package com.ultimatesoftware.banking.people.authentication.configuration;

import com.ultimatesoftware.banking.api.factories.MongoRepositoryFactory;
import com.ultimatesoftware.banking.people.authentication.models.Authentication;

public class AuthenticationRepositoryFactory extends MongoRepositoryFactory<Authentication> {
    public AuthenticationRepositoryFactory() {
        super(Authentication.class);
    }
}
