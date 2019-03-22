package com.ultimatesoftware.banking.people.details.controllers;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ultimatesoftware.banking.api.operations.CrudController;
import com.ultimatesoftware.banking.api.operations.GetController;
import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.people.details.models.PersonDetails;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;

import java.util.List;

@Controller("/api/v1/people")
public class PersonDetailsController implements GetController<PersonDetails>, CrudController<PersonDetails> {

    private final Repository<PersonDetails> mongoRepository;

    public PersonDetailsController(Repository<PersonDetails> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Get
    public Single<List<PersonDetails>> getAll() {
        return mongoRepository.findMany();
    }

    @Get("/{id}")
    public Maybe<PersonDetails> get(String id) {
        return mongoRepository.findOne(id);
    }

    @Post
    public Single<PersonDetails> create(@Valid @Body
        PersonDetails personDetails) {
        return mongoRepository.add(personDetails);
    }

    @Put("/{id}")
    public Maybe<UpdateResult> update(String id, @Valid @Body
        PersonDetails personDetails) {
        return mongoRepository.replaceOne(id, personDetails);
    }

    @Delete("/{id}")
    public Maybe<DeleteResult> delete(String id) {
        return mongoRepository.deleteOne(id);
    }
}
