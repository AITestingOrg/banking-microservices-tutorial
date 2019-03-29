package com.ultimatesoftware.banking.people.cmd.controllers;

import com.ultimatesoftware.banking.people.cmd.commands.CreatePersonCommand;
import com.ultimatesoftware.banking.people.cmd.commands.ResetPasswordCommand;
import com.ultimatesoftware.banking.people.cmd.commands.UpdateDetailsCommand;
import com.ultimatesoftware.banking.people.cmd.models.AuthenticationDto;
import com.ultimatesoftware.banking.people.cmd.models.CreatePersonDto;
import com.ultimatesoftware.banking.people.cmd.models.PersonDetailsDto;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
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

    @Put("/details")
    public void updateDetails(@Valid @Body PersonDetailsDto personDetailsDto) {
        UpdateDetailsCommand command = new UpdateDetailsCommand(personDetailsDto);
        commandGateway.send(command);
    }

    @Put("/reset")
    public void resetPassword(String id, @Valid @Body AuthenticationDto authenticationDto) {
        ResetPasswordCommand command = new ResetPasswordCommand(authenticationDto);
        commandGateway.send(command);
    }
}
