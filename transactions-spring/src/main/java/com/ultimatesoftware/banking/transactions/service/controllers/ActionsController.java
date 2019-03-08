package com.ultimatesoftware.banking.transactions.service.controllers;

import com.ultimatesoftware.banking.transactions.domain.exceptions.CustomerDoesNotExistException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.domain.models.BankTransaction;
import com.ultimatesoftware.banking.transactions.domain.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class ActionsController {
    private TransactionService transactionService;

    public ActionsController(@Autowired TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestHeader double amount, @RequestHeader UUID accountId, @RequestHeader String customerId) {
        try {
            return new ResponseEntity<>(transactionService.withdraw(customerId, accountId, amount).toString(), HttpStatus.OK);
        } catch (NoAccountExistsException | InsufficientBalanceException | CustomerDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestHeader double amount, @RequestHeader UUID accountId, @RequestHeader String customerId) {
        try {
            return new ResponseEntity<>(transactionService.deposit(customerId, accountId, amount).toString(), HttpStatus.OK);
        } catch (NoAccountExistsException | CustomerDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestHeader double amount, @RequestHeader UUID accountId, @RequestHeader String customerId, @RequestHeader UUID destinationAccountId) {
        try {
            return new ResponseEntity<>(transactionService.transfer(customerId, accountId, destinationAccountId, amount).toString(), HttpStatus.OK);
        } catch (NoAccountExistsException | InsufficientBalanceException | CustomerDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BankTransaction> getTransaction(@PathVariable("id") String id) {
        BankTransaction bankTransaction = this.transactionService.getTransaction(id);
        if(bankTransaction != null) {
            return new ResponseEntity<>(bankTransaction, HttpStatus.OK);
        }
        return new ResponseEntity<>(bankTransaction, HttpStatus.BAD_REQUEST);
    }
}
