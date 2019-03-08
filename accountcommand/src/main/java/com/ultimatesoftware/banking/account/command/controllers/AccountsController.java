package com.ultimatesoftware.banking.account.command.controllers;

import com.ultimatesoftware.banking.account.command.commands.*;
import com.ultimatesoftware.banking.account.command.models.AccountDto;
import com.ultimatesoftware.banking.account.command.models.TransactionDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import java.util.UUID;
import javax.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;

@Controller("/api/v1/accounts")
public class AccountsController {

    private CommandGateway commandGateway;

    public AccountsController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Post("/")
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
