package ultimatesoftware.banking.customers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ultimatesoftware.banking.customers.models.Customer;

import java.util.List;

@Controller
@RequestMapping("api")
@EnableMongoRepositories
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    @PostMapping("customers")
    public @ResponseBody Customer addCustomer(@RequestBody Customer customer){
        repository.save(customer);

        return customer;
    }

    @GetMapping("customers")
    public @ResponseBody List<Customer> getCustomers(){
        return repository.findAll();
    }

    @GetMapping("customers/{id}")
    public @ResponseBody Customer getCustomer(@PathVariable("id") String id){
        return repository.findById(id);
    }

    @GetMapping("customers/{id}")
    public @ResponseBody void deleteCustomer(@PathVariable("id") String id){
        repository.delete(id);
    }
}
