package ultimatesoftware.banking.customers.domain.models;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import ultimatesoftware.banking.customers.domain.commands.CreateCustomerCommand;
import ultimatesoftware.banking.customers.domain.events.CustomerCreatedEvent;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

public class CustomerAggregate {

    @AggregateIdentifier
    private UUID id;
    private String firstName = "";
    private String lastName = "";

    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand) {
        apply(new CustomerCreatedEvent(createCustomerCommand.getId(), createCustomerCommand.getFirstName(), createCustomerCommand.getLastName()));
    }

    @EventSourcingHandler
    public void on(CustomerCreatedEvent event) {
        id = event.getId();
        firstName = event.getFirstName();
        lastName = event.getLastName();
    }

    public CustomerAggregate(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public CustomerAggregate() {}

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
