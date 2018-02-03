package ultimatesoftware.banking.customers.domain.commands;

import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import ultimatesoftware.banking.customers.domain.models.CustomerAggregate;

public class CustomerCommandHandler {
    public CustomerCommandHandler(EventSourcingRepository repository, CommandBus commandBus) {
        AggregateAnnotationCommandHandler<CustomerAggregate> handler =
                new AggregateAnnotationCommandHandler<CustomerAggregate>(
                        CustomerAggregate.class, repository);
        handler.subscribe(commandBus);
    }
}
