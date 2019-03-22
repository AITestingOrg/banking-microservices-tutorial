package com.ultimatesoftware.banking.people.cmd.controllers;

import com.ultimatesoftware.banking.people.cmd.commands.CreatePersonCommand;
import com.ultimatesoftware.banking.people.cmd.models.CreatePersonDto;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.validation.Valid;

@Controller("/api/v1/people")
public class PersonController {
    private CommandGateway commandGateway;

    public PersonController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Post
    public String create(@Valid @Body CreatePersonDto person) {
        CreatePersonCommand command = new CreatePersonCommand(person);
        commandGateway.send(command);
        return command.getId().toHexString();
    }
}
