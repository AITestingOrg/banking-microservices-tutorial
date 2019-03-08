package com.ultimatesoftware.banking.customers.controllers;

import com.ultimatesoftware.banking.api.operations.RestController;
import com.ultimatesoftware.banking.api.repository.MongoRepository;
import com.ultimatesoftware.banking.customers.models.Customer;
import io.micronaut.http.annotation.*;

import io.reactivex.Maybe;
import io.reactivex.Single;
import javax.validation.Valid;

import java.util.List;

@Controller("/api/v1")
public class CustomerController implements RestController<Customer> {

    private final MongoRepository<Customer> mongoRepository;

    public CustomerController(MongoRepository<Customer> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Get("customers")
    public Single<List<Customer>> getAll() {
        return mongoRepository.findMany();
    }

    @Get("customers/{id}")
    public Maybe<Customer> get(String id) {
        return mongoRepository.findOne(id);
    }

    @Post("customers")
    public Single<Customer> create(@Valid @Body Customer customer) {
        return mongoRepository.add(customer);
    }

    @Put("customers/{id}")
    public Maybe<Customer> update(String id, @Valid @Body Customer customer) {
        return mongoRepository.replaceOne(id, customer);
    }

    @Delete("customers/{id}")
    public long delete(String id) {
        return mongoRepository.deleteOne(id);
    }
}
