package ultimatesoftware.banking.customers.service.controllers;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ultimatesoftware.banking.customers.domain.commands.CreateCustomerCommand;
import ultimatesoftware.banking.customers.domain.aggregates.Customer;
import ultimatesoftware.banking.customers.domain.commands.DeleteCustomerCommand;
import ultimatesoftware.banking.customers.domain.commands.UpdateCustomerCommand;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("api")
public class CustomerController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("customers")
    public @ResponseBody void addCustomer(@Valid @RequestBody Customer customer){
        commandGateway.send(new CreateCustomerCommand(customer.getFirstName(), customer.getFirstName()));
    }

    @PutMapping("customers")
    public @ResponseBody void deleteCustomer(@Valid @RequestBody Customer customer){
        commandGateway.send(new UpdateCustomerCommand(customer.getId(), customer.getFirstName(), customer.getLastName()));
    }

    @GetMapping("customers")
    public @ResponseBody List<Customer> getCustomers(){
        return new ArrayList<>();
    }

    @GetMapping("customers/{id}")
    public @ResponseBody
    Customer getCustomer(@PathVariable("id") String id){
        return new Customer();
    }

    @DeleteMapping("customers/{id}")
    public @ResponseBody void deleteCustomer(@PathVariable("id") UUID id){
        commandGateway.send(new DeleteCustomerCommand(id));
    }
}
