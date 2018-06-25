package com.ultimatesoftware.banking.account.cmd.service.controllers;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.exceptions.FutureTimeoutException;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountCreationDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountUpdateDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.TransactionDto;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.*;

@RestController
@RequestMapping("api/v1")
public class AccountController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("accounts")
    public UUID addAccount(@Valid @RequestBody AccountCreationDto account) {
        CreateAccountCommand command = new CreateAccountCommand(account.getCustomerId());
        commandGateway.send(command);
        return command.getId();
    }

    @PutMapping("accounts/{id}")
    public void updateAccount(@PathVariable("id") UUID id,
                                                @Valid @RequestBody AccountUpdateDto account) {
        UpdateAccountCommand command = new UpdateAccountCommand(id, account);
        commandGateway.send(command);
    }

    @PutMapping("accounts/debit")
    public void debitAccount(@Valid @RequestBody TransactionDto transaction) {
        DebitAccountCommand command
                = new DebitAccountCommand(transaction.getAccount(), transaction.getAmount(), transaction.getId());
        commandGateway.send(command);
    }

    @PutMapping("accounts/credit")
    public void creditAccount(@Valid @RequestBody TransactionDto transaction) {
        CreditAccountCommand command
                = new CreditAccountCommand(transaction.getAccount(), transaction.getAmount(), transaction.getId());
        commandGateway.send(command);
    }

    @DeleteMapping("accounts/{id}")
    public void deleteAccount(@Valid @PathVariable("id") UUID id) {
        DeleteAccountCommand command = new DeleteAccountCommand(id);
        commandGateway.send(command);
    }

    @PostMapping("transaction/start")
    public void startTransaction(@Valid @RequestBody TransactionDto transaction) {
        StartTransferTransactionCommand command = new StartTransferTransactionCommand(transaction);
        commandGateway.send(command);
    }
}
