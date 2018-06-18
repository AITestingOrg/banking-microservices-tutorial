package com.ultimatesoftware.banking.customers.cmd.service.controllers;

import com.ultimatesoftware.banking.customers.cmd.domain.aggregates.Customer;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.DeleteCustomerCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.CreateCustomerCommand;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.UpdateCustomerCommand;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class CustomerController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("customers")
    public CreateCustomerCommand addCustomer(@Valid @RequestBody Customer customer) {
        CreateCustomerCommand command = new CreateCustomerCommand(customer.getFirstName(), customer.getFirstName());
        commandGateway.send(command);
        return command;
    }

    @PutMapping("customers")
    public UpdateCustomerCommand updateCustomer(@Valid @RequestBody Customer customer) {
        UpdateCustomerCommand command = new UpdateCustomerCommand(customer.getId(),
                                                                  customer.getFirstName(), customer.getLastName());
        commandGateway.send(command);
        return command;
    }

    @DeleteMapping("customers/{id}")
    public DeleteCustomerCommand deleteCustomer(@PathVariable("id") UUID id) {
        DeleteCustomerCommand command = new DeleteCustomerCommand(id);
        commandGateway.send(command);
        return command;
    }
}
