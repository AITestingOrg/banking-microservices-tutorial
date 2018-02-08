package com.ultimatesoftware.banking.account.cmd.service.controllers;

import com.ultimatesoftware.banking.account.cmd.domain.aggregates.Account;
import com.ultimatesoftware.banking.account.cmd.domain.commands.*;
import com.ultimatesoftware.banking.account.cmd.domain.models.Debit;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("api")
public class AccountController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("accounts")
    public void addAccount(@RequestBody Account account){
        commandGateway.send(new CreateAccountCommand(account.getCustomerId()));
    }

    @PutMapping("account/debit/{id}")
    public void debitAccount(@PathVariable("id") String id, @RequestBody Debit debit){
        commandGateway.send(new DebitAccountCommand(debit.getAccountId(), debit.getDebitAmount()));
        commandGateway.send(new OverDraftAccountCommand(debit.getAccountId(), debit.getDebitAmount()));
    }

    @PutMapping("account/credit/{id}")
    public void creditAccount(@PathVariable("id") String id, @RequestBody Account updatedAccount){
        commandGateway.send(new CreditAccountCommand(updatedAccount.getId(), updatedAccount.getBalance()));
    }

    @GetMapping("accounts")
    public @ResponseBody List<Account> getCustomers(){
        return new ArrayList<>();
    }

    @GetMapping("account/{id}")
    public @ResponseBody
    Account getCustomer(@PathVariable("id") String id){
        return null;
    }

    @DeleteMapping("accounts/{id}")
    public void deleteCustomer(@PathVariable("id") UUID id){
        commandGateway.send(new DeleteAccountCommand(id));
    }
}
