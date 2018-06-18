package com.ultimatesoftware.banking.customers.cmd.domain.aggregates;

import com.ultimatesoftware.banking.customer.common.events.CustomerCreatedEvent;
import com.ultimatesoftware.banking.customer.common.events.CustomerDeletedEvent;
import com.ultimatesoftware.banking.customer.common.events.CustomerUpdatedEvent;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.DeleteCustomerCommand;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.UpdateCustomerCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import com.ultimatesoftware.banking.customers.cmd.domain.commands.CreateCustomerCommand;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class Customer {

    @AggregateIdentifier
    private UUID id;
    @Size(min=2, max=30)
    private String firstName;
    @Size(min=2, max=30)
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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
