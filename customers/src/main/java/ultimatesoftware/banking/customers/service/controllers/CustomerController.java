package ultimatesoftware.banking.customers.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ultimatesoftware.banking.customers.domain.models.CustomerAggregate;

import java.util.List;

@Controller
@RequestMapping("api")
@EnableMongoRepositories
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    @PostMapping("customers")
    public @ResponseBody
    CustomerAggregate addCustomer(@RequestBody CustomerAggregate customer){
        repository.save(customer);

        return customer;
    }

    @GetMapping("customers")
    public @ResponseBody List<CustomerAggregate> getCustomers(){
        return repository.findAll();
    }

    @GetMapping("customers/{id}")
    public @ResponseBody
    CustomerAggregate getCustomer(@PathVariable("id") String id){
        return repository.findById(id);
    }

    @DeleteMapping("customers/{id}")
    public @ResponseBody void deleteCustomer(@PathVariable("id") String id){
        repository.delete(id);
    }
}
