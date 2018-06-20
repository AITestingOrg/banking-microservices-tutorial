package com.ultimatesoftware.banking.account.cmd.service.controllers;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountCreationDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountUpdateDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.TransactionDto;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1")
public class AccountController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("accounts")
    public ResponseEntity<String> addAccount(@Valid @RequestBody AccountCreationDto account) {
        CreateAccountCommand command = new CreateAccountCommand(account.getCustomerId());
        return sendCommand(command);
    }

    @PutMapping("accounts/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable("id") UUID id,
                                                @Valid @RequestBody AccountUpdateDto account) {
        UpdateAccountCommand command = new UpdateAccountCommand(id, account);
        return sendCommand(command);
    }

    @PutMapping("accounts/debit")
    public ResponseEntity<String> debitAccount(@Valid @RequestBody TransactionDto transaction) {
        DebitAccountCommand command
                = new DebitAccountCommand(transaction.getAccount(), transaction.getAmount(), transaction.getId());
        return sendCommand(command);
    }

    @PutMapping("accounts/credit")
    public ResponseEntity<String> creditAccount(@Valid @RequestBody TransactionDto transaction) {
        CreditAccountCommand command
                = new CreditAccountCommand(transaction.getAccount(), transaction.getAmount(), transaction.getId());
        return sendCommand(command);
    }

    @DeleteMapping("accounts/{id}")
    public ResponseEntity<String> deleteAccount(@Valid @PathVariable("id") UUID id) {
        DeleteAccountCommand command = new DeleteAccountCommand(id);
        return sendCommand(command);
    }

    private ResponseEntity<String> sendCommand(Command command) {
        CompletableFuture future = commandGateway.send(command);
        if(future.isCompletedExceptionally()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(command.getId().toString(), HttpStatus.OK);
    }
}
