package ultimatesoftware.banking.customers.domain.eventhandlers;

import org.axonframework.eventhandling.EventHandler;
import ultimatesoftware.banking.customers.domain.events.CustomerCreatedEvent;

public class CustomerEventHandler {
    @EventHandler
    public void handle(CustomerCreatedEvent event) {
        System.out.println(String.format("Customer Created: %s %s ID: %s", event.getFirstName(), event.getLastName(), event.getId()));
    }
}
