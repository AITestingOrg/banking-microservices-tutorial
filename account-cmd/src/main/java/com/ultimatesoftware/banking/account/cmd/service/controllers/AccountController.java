package com.ultimatesoftware.banking.account.cmd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.ultimatesoftware.banking.account.cmd.models.Account;

import java.util.List;

@Controller
@RequestMapping("api")
@EnableMongoRepositories
public class AccountController {
    @Autowired
    private AccountRepository repository;

    @PostMapping("accounts")
    public @ResponseBody Account addAccount(@RequestBody Account account){
        repository.save(account);

        return account;
    }

    @PutMapping("account/{id}")
    public @ResponseBody Account updateAccount(@PathVariable("id") String id, @RequestBody Account updatedAccount){
        Account account = repository.findById(id);
        account.balance = updatedAccount.balance;
        repository.save(account);

        return account;
    }

    @GetMapping("accounts")
    public @ResponseBody List<Account> getCustomers(){
        return repository.findAll();
    }

    @GetMapping("account/{id}")
    public @ResponseBody
    Account getCustomer(@PathVariable("id") String id){
        return repository.findById(id);
    }

    @GetMapping("accounts/{id}")
    public @ResponseBody void deleteCustomer(@PathVariable("id") String id){
        repository.delete(id);
    }
}
