package com.ultimatesoftware.banking.account.query.controllers;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.account.query.models.Account;
import com.ultimatesoftware.banking.api.operations.RestController;
import com.ultimatesoftware.banking.api.repository.Repository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import javax.validation.Valid;

@Controller("/api/v1/accounts")
public class AccountsController implements RestController<Account> {

    private final Repository<Account> mongoRepository;

    public AccountsController(Repository<Account> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Get
    @Override public Single<List<Account>> getAll() {
        return mongoRepository.findMany();
    }

    @Get("/{id}")
    @Override public Maybe<Account> get(String id) {
        return mongoRepository.findOne(id);
    }

    @Post
    @Override public Single<Account> create(@Valid Account account) {
        return mongoRepository.add(account);
    }

    @Put("/{id}")
    @Override public Maybe<UpdateResult> update(String id, @Valid Account account) {
        return mongoRepository.replaceOne(id, account);
    }

    @Delete("/{id}")
    @Override public Maybe<DeleteResult> delete(String id) {
        return mongoRepository.deleteOne(id);
    }
}
