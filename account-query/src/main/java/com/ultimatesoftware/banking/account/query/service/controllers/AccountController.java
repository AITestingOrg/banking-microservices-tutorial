package com.ultimatesoftware.banking.account.query.service.controllers;

import com.ultimatesoftware.banking.account.query.domain.models.Account;
import com.ultimatesoftware.banking.account.query.service.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("accounts")
    public @ResponseBody
    List<Account> getCustomers(){
        return accountRepository.findAll();
    }

    @GetMapping("account/{id}")
    public @ResponseBody
    Account getCustomer(@PathVariable("id") UUID id){
        return accountRepository.findOne(id);
    }
}
