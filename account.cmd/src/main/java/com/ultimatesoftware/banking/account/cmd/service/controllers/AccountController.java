package com.ultimatesoftware.banking.account.cmd.service.controllers;

import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountCreationDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountUpdateDto;
import com.ultimatesoftware.banking.account.cmd.domain.models.TransactionAmount;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class AccountController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("account")
    public CreateAccountCommand addAccount(@Valid @RequestBody AccountCreationDto account) {
        CreateAccountCommand command = new CreateAccountCommand(account.getCustomerId());
        commandGateway.send(command);
        return command;
    }

    @PutMapping("account/{id}")
    public UpdateAccountCommand debitAccount(@PathVariable("id") UUID id,
                                            @Valid @RequestBody AccountUpdateDto account) {
        UpdateAccountCommand command = new UpdateAccountCommand(id, account);
        commandGateway.send(command);
        return command;
    }

    @PutMapping("account/{id}/debit")
    public DebitAccountCommand debitAccount(@PathVariable("id") UUID id,
                                            @Valid @RequestBody TransactionAmount amount) {
        DebitAccountCommand command = new DebitAccountCommand(id, amount.getAmount());
        commandGateway.send(command);
        commandGateway.send(new OverDraftAccountCommand(id, amount.getAmount()));
        return command;
    }

    @PutMapping("account/{id}/credit")
    public CreditAccountCommand creditAccount(@Valid @PathVariable("id") UUID id,
                                              @Valid @RequestBody TransactionAmount amount) {
        CreditAccountCommand command = new CreditAccountCommand(id, amount.getAmount());
        commandGateway.send(command);
        return command;
    }

    @DeleteMapping("account/{id}")
    public DeleteAccountCommand deleteAccount(@Valid @PathVariable("id") UUID id) {
        DeleteAccountCommand command = new DeleteAccountCommand(id);
        commandGateway.send(command);
        return command;
    }
}
