package ultimatesoftware.banking.customers.domain.eventhandlers;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ultimatesoftware.banking.customers.domain.events.CustomerCreatedEvent;
import ultimatesoftware.banking.customers.service.configuration.CustomerAmqpEventConfiguration;

@Component
public class CustomerEventHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(CustomerAmqpEventConfiguration.class);
    @EventHandler
    public void handle(CustomerCreatedEvent event) {
        LOG.info(String.format("Customer Created: %s %s ID: %s", event.getFirstName(), event.getLastName(), event.getId()));
    }
}
