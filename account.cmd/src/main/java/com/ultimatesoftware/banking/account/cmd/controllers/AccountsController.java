package com.ultimatesoftware.banking.account.cmd.controllers;

import com.ultimatesoftware.banking.account.cmd.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.commands.*;
import com.ultimatesoftware.banking.account.cmd.models.AccountDto;
import com.ultimatesoftware.banking.account.cmd.models.TransactionDto;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import java.util.UUID;
import javax.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api/v1/accounts")
public class AccountsController {
    Logger logger = LoggerFactory.getLogger(AccountsController.class);
    private CommandGateway commandGateway;

    public AccountsController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
        logger.info("Logger started on controller");
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        return "Hello";
    }

    @Post
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@Valid AccountDto account) {
        CreateAccountCommand command = new CreateAccountCommand(account.getCustomerId());
        this.commandGateway.send(command);
        return command.getId().toString();
    }

    @Put("/debit")
    public void debit(@Valid TransactionDto transaction) {
        DebitAccountCommand command = new DebitAccountCommand(transaction.getAccount(), transaction.getAmount(), transaction.getId());
        this.commandGateway.send(command);
    }

    @Put("/credit")
    public void credit(@Valid TransactionDto transaction) {
        CreditAccountCommand command = new CreditAccountCommand(transaction.getAccount(), transaction.getAmount(), transaction.getId());
        this.commandGateway.send(command);
    }

    @Put("/transfer")
    public void transfer(@Valid TransactionDto transaction) {
        StartTransferTransactionCommand command = new StartTransferTransactionCommand(transaction);
        this.commandGateway.send(command);
    }

    @Delete("/{id}")
    public void delete(UUID id) {
        DeleteAccountCommand command = new DeleteAccountCommand(id);
        this.commandGateway.send(command);
    }
}
