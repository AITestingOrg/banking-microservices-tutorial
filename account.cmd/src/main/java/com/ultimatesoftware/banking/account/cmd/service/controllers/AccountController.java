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
    @Autowired
    private ThreadPoolTaskScheduler executor;

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

    @PostMapping("transaction/start")
    public ResponseEntity<?> startTransaction(@Valid @RequestBody TransactionDto transaction) {
        StartTransferTransactionCommand command = new StartTransferTransactionCommand(transaction);
        CompletableFuture.supplyAsync(() -> commandGateway.send(command), executor)
                .acceptEither(timeoutAfter(3000),
                        fail -> commandGateway.send(
                                new FailToStartTransferTransactionCommand(transaction)));
        return new ResponseEntity<>(command.getTransactionId(), HttpStatus.OK);
    }

    private ResponseEntity<String> sendCommand(Command command) {
        CompletableFuture future = commandGateway.send(command);
        if(future.isCompletedExceptionally()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(command.getId().toString(), HttpStatus.OK);
    }

    private CompletableFuture timeoutAfter(long timeout) {
        CompletableFuture result = new CompletableFuture();
        executor.schedule(new FutureTimeoutException(result),
                new Date(System.currentTimeMillis() + timeout));
        return result;
    }
}
