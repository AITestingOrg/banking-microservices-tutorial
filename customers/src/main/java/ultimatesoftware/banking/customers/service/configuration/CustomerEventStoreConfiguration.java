package ultimatesoftware.banking.customers.service.configuration;

import com.ultimatesoftware.banking.eventsourcing.configurations.EventStoreConfiguration;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.context.annotation.Configuration;
import ultimatesoftware.banking.customers.domain.commands.CustomerCommandHandler;
import ultimatesoftware.banking.customers.domain.models.CustomerAggregate;


@Configuration
public class CustomerEventStoreConfiguration extends EventStoreConfiguration<CustomerAggregate, CustomerCommandHandler> {

    @Override
    public CustomerCommandHandler commandHandler(EventSourcingRepository eventSourcingRepository, CommandBus commandBus) {
        return new CustomerCommandHandler(eventSourcingRepository, commandBus);
    }
}
