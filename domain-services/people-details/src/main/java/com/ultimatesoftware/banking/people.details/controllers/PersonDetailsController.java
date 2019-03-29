package com.ultimatesoftware.banking.people.details.controllers;

import com.ultimatesoftware.banking.api.operations.GetController;
import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.people.details.models.PersonDetails;
import io.micronaut.http.annotation.*;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Controller("/api/v1/people")
public class PersonDetailsController implements GetController<PersonDetails> {

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
}
