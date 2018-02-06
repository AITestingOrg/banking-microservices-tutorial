package com.ultimatesoftware.banking.customerscmd.domain.aggregates;

import com.ultimatesoftware.banking.customerscmd.domain.commands.UpdateCustomerCommand;
import com.ultimatesoftware.banking.customerscmd.domain.events.CustomerCreatedEvent;
import com.ultimatesoftware.banking.customerscmd.domain.events.CustomerDeletedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import com.ultimatesoftware.banking.customerscmd.domain.commands.CreateCustomerCommand;
import com.ultimatesoftware.banking.customerscmd.domain.commands.DeleteCustomerCommand;
import com.ultimatesoftware.banking.customerscmd.domain.events.CustomerUpdatedEvent;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class Customer {

    @AggregateIdentifier
    private UUID id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @CommandHandler
    public Customer(CreateCustomerCommand createCustomerCommand) {
        apply(new CustomerCreatedEvent(createCustomerCommand.getId(), createCustomerCommand.getFirstName(), createCustomerCommand.getLastName()));
    }

    @EventSourcingHandler
    public void on(CustomerCreatedEvent event) {
        id = event.getId();
        firstName = event.getFirstName();
        lastName = event.getLastName();
    }

    public Customer(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer() {}

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @CommandHandler
    public void on(DeleteCustomerCommand command) {
        apply(new CustomerDeletedEvent(id));
    }

    @CommandHandler
    public void on(UpdateCustomerCommand command) {
        apply(new CustomerUpdatedEvent(command.getId(), command.getFirstName(), command.getLastName()));
    }

    @EventSourcingHandler
    public void on(CustomerDeletedEvent event) {
        markDeleted();
    }

    @EventSourcingHandler
    public void on(CustomerUpdatedEvent event) {
        firstName = event.getFirstName();
        lastName = event.getLastName();
    }
}
