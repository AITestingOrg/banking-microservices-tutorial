package ultimatesoftware.banking.customers.service.controllers;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ultimatesoftware.banking.customers.domain.commands.CreateCustomerCommand;
import ultimatesoftware.banking.customers.domain.models.CustomerAggregate;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("api")
@EnableMongoRepositories
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("customers")
    public @ResponseBody
    CustomerAggregate addCustomer(@Valid @RequestBody CustomerAggregate customer){
        commandGateway.send(new CreateCustomerCommand(customer.getFirstName(), customer.getFirstName()));

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
