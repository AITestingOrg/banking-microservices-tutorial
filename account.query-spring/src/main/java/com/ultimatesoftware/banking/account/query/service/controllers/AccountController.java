package com.ultimatesoftware.banking.account.query.service.controllers;

import com.ultimatesoftware.banking.account.query.domain.models.Account;
import com.ultimatesoftware.banking.account.query.service.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("accounts/{id}")
    public ResponseEntity<Account> getAccount(@Valid @PathVariable("id") UUID id) {
        Optional<Account> account = accountRepository.findByAccountId(id);
        if (account.isPresent()) {
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
