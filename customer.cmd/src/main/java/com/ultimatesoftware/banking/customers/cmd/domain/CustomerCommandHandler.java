package com.ultimatesoftware.banking.customers.cmd.domain;

import com.ultimatesoftware.banking.customers.cmd.domain.aggregates.Customer;
import com.ultimatesoftware.banking.eventsourcing.handlers.CustomCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;

public class CustomerCommandHandler extends CustomCommandHandler<Customer> {
    public CustomerCommandHandler(EventSourcingRepository repository, CommandBus commandBus) {
        super(repository, commandBus, Customer.class);
    }
}
