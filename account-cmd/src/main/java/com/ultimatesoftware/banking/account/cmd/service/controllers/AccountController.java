package com.ultimatesoftware.banking.account.cmd.service.controllers;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountCreationDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.Debit;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
public class AccountController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("account")
    public void addAccount(@Valid @RequestBody AccountCreationDto account) {
        commandGateway.send(new CreateAccountCommand(account.getCustomerId()));
    }

    @PutMapping("account/{id}/debit")
    public void debitAccount(@PathVariable("id") UUID id, @Valid @RequestBody Debit debit) {
        commandGateway.send(new DebitAccountCommand(debit.getAccountId(), debit.getDebitAmount()));
        commandGateway.send(new OverDraftAccountCommand(debit.getAccountId(), debit.getDebitAmount()));
    }

    @PutMapping("account/{id}/credit")
    public void creditAccount(@Valid @PathVariable("id") UUID id, @Valid @RequestBody Account updatedAccount) {
        commandGateway.send(new CreditAccountCommand(updatedAccount.getId(), updatedAccount.getBalance()));
    }

    @DeleteMapping("account/{id}")
    public void deleteAccount(@Valid @PathVariable("id") UUID id) {
        commandGateway.send(new DeleteAccountCommand(id));
    }
}
