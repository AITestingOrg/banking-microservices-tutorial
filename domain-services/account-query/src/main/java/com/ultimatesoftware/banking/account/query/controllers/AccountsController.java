package com.ultimatesoftware.banking.account.query.controllers;

import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.operations.GetController;
import com.ultimatesoftware.banking.api.repository.Repository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Controller("/api/v1/accounts")
public class AccountsController implements GetController<Account> {

    private final Repository<Account> mongoRepository;

    public AccountsController(Repository<Account> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Get
    public Single<List<Account>> getAll() {
        return mongoRepository.findMany();
    }

    @Get("/{id}")
    public Maybe<Account> get(String id) {
        return mongoRepository.findOne(id);
    }
}
