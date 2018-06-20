package com.ultimatesoftware.banking.transactions.domain.controllers;

import com.ultimatesoftware.banking.transactions.domain.exceptions.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ultimatesoftware.banking.transactions.domain.exceptions.NoAccountExistsException;
import com.ultimatesoftware.banking.transactions.domain.services.TransactionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class ActionsController {
    @Autowired
    private TransactionService transactionService;

    public ActionsController() {}

    @GetMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestHeader double amount, @RequestHeader UUID accountId, @RequestHeader UUID customerId) {
        try {
            return new ResponseEntity<>(transactionService.withdraw(customerId, accountId, amount).toString(), HttpStatus.OK);
        } catch(NoAccountExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestHeader double amount, @RequestHeader UUID accountId, @RequestHeader UUID customerId) {
        try {
            return new ResponseEntity<>(transactionService.deposit(customerId, accountId, amount).toString(), HttpStatus.OK);
        } catch(NoAccountExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestHeader double amount, @RequestHeader UUID accountId, @RequestHeader UUID customerId, @RequestHeader UUID destinationAccountId) {
        try {
            return new ResponseEntity<>(transactionService.transfer(customerId, accountId, destinationAccountId, amount).toString(), HttpStatus.OK);
        } catch(NoAccountExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(InsufficientBalanceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
