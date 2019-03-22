package com.ultimatesoftware.banking.people.authentication.controllers;

import com.ultimatesoftware.banking.api.repository.Repository;
import com.ultimatesoftware.banking.people.authentication.models.Authentication;
import com.ultimatesoftware.banking.people.authentication.models.AuthenticationDto;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import javax.validation.Valid;

@Controller("/api/v1/login")
public class LoginController {
    private Repository<Authentication> mongoRepository;

    public LoginController(
        Repository<Authentication> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Post
    public void login(@Valid @Body AuthenticationDto authentication) {

    }

}
