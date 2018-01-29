package ultimatesoftware.banking.customers.service.configuration;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ultimatesoftware.banking.customers.domain.models.Customer;

@Configuration
public class AxonConfiguration {
    @Bean
    public Repository<Customer> eventSourcingRepository(EventStore eventStore)
    {
        EventSourcingRepository<Customer> repository = new EventSourcingRepository<>(Customer.class, eventStore);
        return repository;
    }
}
