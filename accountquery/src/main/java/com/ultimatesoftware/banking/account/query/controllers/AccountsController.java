package com.ultimatesoftware.banking.account.query.controllers;

import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.account.query.repositories.AccountMongoRepository;
import com.ultimatesoftware.banking.api.operations.RestController;
import io.micronaut.http.annotation.Controller;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import javax.validation.Valid;

@Controller("/api/v1/accounts")
public class AccountsController implements RestController<Account> {
    private final AccountMongoRepository mongoRepository;

    public AccountsController(AccountMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override public Single<List<Account>> getAll() {
        return mongoRepository.findMany();
    }

    @Override public Maybe<Account> get(String id) {
        return mongoRepository.findByAccountId(id);
    }

    @Override public Single<Account> create(@Valid Account account) {
        return mongoRepository.add(account);
    }

    @Override public Maybe<Account> update(String id, @Valid Account account) {
        return mongoRepository.replaceOne(id, account);
    }

    @Override public long delete(String id) {
        return mongoRepository.deleteByAccountId(id);
    }
}
