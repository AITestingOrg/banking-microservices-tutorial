package ultimatesoftware.banking.customers.domain.aggregates;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import ultimatesoftware.banking.customers.domain.commands.CreateCustomerCommand;
import ultimatesoftware.banking.customers.domain.events.CustomerCreatedEvent;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private UUID id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

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
