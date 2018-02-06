package com.ultimatesoftware.banking.customerscmd.domain;

import com.ultimatesoftware.banking.eventsourcing.handlers.CustomCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import com.ultimatesoftware.banking.customerscmd.domain.aggregates.Customer;

public class CustomerCommandHandler extends CustomCommandHandler<Customer> {
    public CustomerCommandHandler(EventSourcingRepository repository, CommandBus commandBus) {
        super(repository, commandBus, Customer.class);
    }
}
