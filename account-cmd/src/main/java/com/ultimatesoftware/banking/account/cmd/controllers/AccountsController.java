package com.ultimatesoftware.banking.account.cmd.controllers;

import com.ultimatesoftware.banking.account.cmd.commands.*;
import com.ultimatesoftware.banking.account.cmd.models.AccountDto;
import com.ultimatesoftware.banking.account.cmd.models.MessageDto;
import com.ultimatesoftware.banking.account.cmd.models.TransactionDto;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.validation.Valid;

@Controller("/api/v1/accounts")
public class AccountsController {
    private CommandGateway commandGateway;
    private static final String SUCCESS = "SENT COMMAND";

    public AccountsController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Post
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@Valid AccountDto account) {
        CreateAccountCommand command = new CreateAccountCommand(account.getCustomerId());
        this.commandGateway.send(command);
        return command.getId().toString();
    }

    @Put("/debit")
    @Produces(MediaType.TEXT_PLAIN)
    public MessageDto debit(@Valid TransactionDto transaction) {
        DebitAccountCommand command = new DebitAccountCommand(transaction.getAccountId(), transaction.getAmount(), transaction.getId());
        this.commandGateway.send(command);
        return new MessageDto(SUCCESS);
    }

    @Put("/credit")
    @Produces(MediaType.TEXT_PLAIN)
    public MessageDto credit(@Valid TransactionDto transaction) {
        CreditAccountCommand command = new CreditAccountCommand(transaction.getAccountId(), transaction.getAmount(), transaction.getId());
        this.commandGateway.send(command);
        return new MessageDto(SUCCESS);
    }

    @Put("/transfer")
    @Produces(MediaType.TEXT_PLAIN)
    public MessageDto transfer(@Valid TransactionDto transaction) {
        StartTransferTransactionCommand command = new StartTransferTransactionCommand(transaction);
        this.commandGateway.send(command);
        return new MessageDto(SUCCESS);
    }

    @Delete("/{id}")
    public void delete(String id) {
        DeleteAccountCommand command = new DeleteAccountCommand(id);
        this.commandGateway.send(command);
    }

    @Get("/alive")
    public String alive() {
        return "Service alive";
    }
}
