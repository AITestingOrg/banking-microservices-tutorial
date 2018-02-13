package com.ultimatesoftware.banking.customers.cmd.service.controllers;

import com.ultimatesoftware.banking.customers.cmd.domain.aggregates.Customer;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.DeleteCustomerCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.CreateCustomerCommand;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.UpdateCustomerCommand;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("api/v1")
public class CustomerController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("customer")
    public @ResponseBody void addCustomer(@Valid @RequestBody Customer customer){
        commandGateway.send(new CreateCustomerCommand(customer.getFirstName(), customer.getFirstName()));
    }

    @PutMapping("customer")
    public @ResponseBody void deleteCustomer(@Valid @RequestBody Customer customer){
        commandGateway.send(new UpdateCustomerCommand(customer.getId(), customer.getFirstName(), customer.getLastName()));
    }

    @DeleteMapping("customer/{id}")
    public @ResponseBody void deleteCustomer(@PathVariable("id") UUID id){
        commandGateway.send(new DeleteCustomerCommand(id));
    }
}
