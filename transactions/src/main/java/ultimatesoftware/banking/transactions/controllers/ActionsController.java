package ultimatesoftware.banking.transactions.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ultimatesoftware.banking.transactions.models.ActionResult;
import ultimatesoftware.banking.transactions.services.TransactionService;

@Controller
@RequestMapping("/api/transactions")
public class ActionsController {
    @Autowired
    private TransactionService transactionService;

    public ActionsController() {}

    @RequestMapping("/withdraw")
    public @ResponseBody String withdraw(@RequestHeader double amount, @RequestHeader String accountId, @RequestHeader String customerId) {
        return transactionService.withdraw(accountId, customerId, amount).toString();
    }

    @RequestMapping("/deposit")
    public @ResponseBody String deposit(@RequestHeader double amount, @RequestHeader String accountId, @RequestHeader String customerId) {
        return transactionService.deposit(customerId, accountId, amount).toString();
    }

    @RequestMapping("/transfer")
    public @ResponseBody String transfer(@RequestHeader double amount, @RequestHeader String accountId, @RequestHeader String customerId, @RequestHeader String destinationAccountId) {
        return transactionService.transfer(customerId, accountId, destinationAccountId, amount).toString();
    }
}
