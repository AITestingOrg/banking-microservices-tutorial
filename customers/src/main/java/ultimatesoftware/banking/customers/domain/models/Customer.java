package ultimatesoftware.banking.customers.domain.models;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import ultimatesoftware.banking.customers.domain.events.CustomerCreatedEvent;

import java.util.UUID;

public class Customer {

    @AggregateIdentifier
    private UUID id;
    private String firstName = "";
    private String lastName = "";

    public Customer(){
    }

    public Customer(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @EventSourcingHandler
    public void on(CustomerCreatedEvent event) {
        id = event.getId();
        firstName = event.getFirstName();
        lastName = event.getLastName();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
