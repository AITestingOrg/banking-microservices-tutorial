package ultimatesoftware.banking.transactions.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ultimatesoftware.banking.transactions.services.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class ActionsController {
    @Autowired
    private TransactionService transactionService;

    public ActionsController() {}

    @GetMapping("/withdraw")
    public String withdraw(@RequestHeader double amount, @RequestHeader String accountId, @RequestHeader String customerId) {
        return transactionService.withdraw(customerId, accountId, amount).toString();
    }

    @GetMapping("/deposit")
    public String deposit(@RequestHeader double amount, @RequestHeader String accountId, @RequestHeader String customerId) {
        return transactionService.deposit(customerId, accountId, amount).toString();
    }

    @GetMapping("/transfer")
    public String transfer(@RequestHeader double amount, @RequestHeader String accountId, @RequestHeader String customerId, @RequestHeader String destinationAccountId) {
        return transactionService.transfer(customerId, accountId, destinationAccountId, amount).toString();
    }
}
