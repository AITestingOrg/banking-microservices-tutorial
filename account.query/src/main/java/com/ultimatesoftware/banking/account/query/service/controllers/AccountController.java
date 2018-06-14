package com.ultimatesoftware.banking.account.query.service.controllers;

import com.ultimatesoftware.banking.account.query.domain.models.Account;
import com.ultimatesoftware.banking.account.query.service.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("accounts")
    public List<Account> getCustomers() {
        return accountRepository.findAll();
    }

    @GetMapping("account/{id}")
    public Account getCustomer(@Valid @PathVariable("id") UUID id) {
        return accountRepository.findOne(id);
    }
}
