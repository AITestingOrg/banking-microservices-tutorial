package ultimatesoftware.banking.transactions.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by justinp on 7/21/17.
 */
@Controller
@RequestMapping("/api/transactions")
public class ActionsController {
    @RequestMapping("/withdraw")
    public @ResponseBody boolean withdraw(@RequestHeader int amount, @RequestHeader int accountId, @RequestHeader String customerId) {

    }

    @RequestMapping("/deposit")
    public @ResponseBody boolean deposit(@RequestHeader int amount, @RequestHeader int accountId, @RequestHeader String customerId) {

    }

    @RequestMapping("/transfer")
    public @ResponseBody boolean transfer(@RequestHeader int amount, @RequestHeader int accountId, @RequestHeader String customerId, @RequestHeader int targetAccountId) {

    }
}
