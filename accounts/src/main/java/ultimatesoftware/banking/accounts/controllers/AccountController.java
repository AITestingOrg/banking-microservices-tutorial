package ultimatesoftware.banking.accounts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ultimatesoftware.banking.accounts.models.Account;

import java.util.List;

@Controller
@RequestMapping("api")
@EnableMongoRepositories
public class AccountController {
    @Autowired
    private AccountRepository repository;

    @PostMapping("accounts")
    public @ResponseBody Account addCustomer(@RequestBody Account account){
        repository.save(account);

        return account;
    }

    @GetMapping("accounts")
    public @ResponseBody List<Account> getCustomers(){
        return repository.findAll();
    }

    @GetMapping("accounts/{id}")
    public @ResponseBody
    Account getCustomer(@PathVariable("id") String id){
        return repository.findById(id);
    }

    @GetMapping("accounts/{id}")
    public @ResponseBody void deleteCustomer(@PathVariable("id") String id){
        repository.delete(id);
    }
}
