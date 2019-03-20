package com.ultimatesoftware.banking.customers.controllers;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.api.operations.CrudController;
import com.ultimatesoftware.banking.api.operations.GetController;
import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.customers.models.Customer;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;

import java.util.List;

@Controller("/api/v1/customers")
public class CustomerController implements GetController<Customer>, CrudController<Customer> {

    private final Repository<Customer> mongoRepository;

    public CustomerController(Repository<Customer> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Get
    public Single<List<Customer>> getAll() {
        return mongoRepository.findMany();
    }

    @Get("/{id}")
    public Maybe<Customer> get(String id) {
        return mongoRepository.findOne(id);
    }

    @Post
    public Single<Customer> create(@Valid @Body Customer customer) {
        return mongoRepository.add(customer);
    }

    @Put("/{id}")
    public Maybe<UpdateResult> update(String id, @Valid @Body Customer customer) {
        return mongoRepository.replaceOne(id, customer);
    }

    @Delete("/{id}")
    public Maybe<DeleteResult> delete(String id) {
        return mongoRepository.deleteOne(id);
    }
}
