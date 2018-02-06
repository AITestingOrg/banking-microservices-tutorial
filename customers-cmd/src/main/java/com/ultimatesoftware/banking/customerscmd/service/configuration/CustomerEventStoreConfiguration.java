package ultimatesoftware.banking.customers.service.configuration;

import com.ultimatesoftware.banking.eventsourcing.configurations.EventStoreConfiguration;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ultimatesoftware.banking.customers.domain.CustomerCommandHandler;
import ultimatesoftware.banking.customers.domain.aggregates.Customer;


@Configuration
public class CustomerEventStoreConfiguration extends EventStoreConfiguration<Customer, CustomerCommandHandler> {
    public CustomerEventStoreConfiguration() {
        super(Customer.class);
    }

    @Override
    @Bean
    public CustomerCommandHandler commandHandler(EventSourcingRepository eventSourcingRepository, CommandBus commandBus) {
        CustomerCommandHandler commandHandler = new CustomerCommandHandler(eventSourcingRepository, commandBus);
        commandHandler.subscribe();
        return commandHandler;
    }
}
