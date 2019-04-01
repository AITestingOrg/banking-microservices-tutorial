package com.ultimatesoftware.banking.account.cmd.controllers;

import com.ultimatesoftware.banking.account.cmd.clients.PeopleDetailsClient;
import com.ultimatesoftware.banking.account.cmd.commands.*;
import com.ultimatesoftware.banking.account.cmd.exceptions.PersonNotFoundException;
import com.ultimatesoftware.banking.account.cmd.models.AccountDto;
import com.ultimatesoftware.banking.account.cmd.models.MessageDto;
import com.ultimatesoftware.banking.account.cmd.models.PersonDetailsDto;
import com.ultimatesoftware.banking.account.cmd.models.TransactionDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.validation.Valid;

@Controller("/api/v1/accounts")
public class AccountsController {
    private CommandGateway commandGateway;
    private PeopleDetailsClient peopleDetailsClient;
    private static final String SUCCESS = "SENT COMMAND";

    public AccountsController(CommandGateway commandGateway, PeopleDetailsClient peopleDetailsClient) {
        this.commandGateway = commandGateway;
        this.peopleDetailsClient = peopleDetailsClient;
    }

    @Post
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@Valid AccountDto account) throws PersonNotFoundException {
        PersonDetailsDto customer = peopleDetailsClient.get(account.getCustomerId()).blockingGet();
        if (customer != null) {
            CreateAccountCommand command = new CreateAccountCommand(account.getCustomerId());
            this.commandGateway.send(command);
            return command.getId().toString();
        }
        throw new PersonNotFoundException(account.getCustomerId(), "Person not found during creation of account.");
    }

    @Put("/debit")
    public HttpResponse<MessageDto> debit(@Valid TransactionDto transaction) {
        DebitAccountCommand command = new DebitAccountCommand(transaction.getAccountId(), transaction.getAmount(), transaction.getId());
        this.commandGateway.send(command);
        return HttpResponse.ok(new MessageDto(SUCCESS));
    }

    @Put("/credit")
    public HttpResponse<MessageDto> credit(@Valid TransactionDto transaction) {
        CreditAccountCommand command = new CreditAccountCommand(transaction.getAccountId(), transaction.getAmount(), transaction.getId());
        this.commandGateway.send(command);
        return HttpResponse.ok(new MessageDto(SUCCESS));
    }

    @Put("/transfer")
    public HttpResponse<MessageDto> transfer(@Valid TransactionDto transaction) {
        StartTransferCommand command = new StartTransferCommand(transaction);
        this.commandGateway.send(command);
        return HttpResponse.ok(new MessageDto(SUCCESS));
    }

    @Delete("/{id}")
    public void delete(String id) {
        DeleteAccountCommand command = new DeleteAccountCommand(id);
        this.commandGateway.send(command);
    }
}
