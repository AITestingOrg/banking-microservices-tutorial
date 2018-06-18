package ultimatesoftware.banking.transactions.domain.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimatesoftware.banking.transactions.domain.services.TransactionService;

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
            return new ResponseEntity<>(transactionService.withdraw(customerId, accountId, amount), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestHeader double amount, @RequestHeader UUID accountId, @RequestHeader UUID customerId) {
        try {
            return transactionService.deposit(customerId, accountId, amount).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestHeader double amount, @RequestHeader UUID accountId, @RequestHeader UUID customerId, @RequestHeader UUID destinationAccountId) {
        try {
            return transactionService.transfer(customerId, accountId, destinationAccountId, amount).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
