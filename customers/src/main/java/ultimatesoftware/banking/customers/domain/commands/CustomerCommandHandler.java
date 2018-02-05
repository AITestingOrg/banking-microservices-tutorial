package ultimatesoftware.banking.customers.domain.commands;

import com.ultimatesoftware.banking.eventsourcing.handlers.CommandHandler;
import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import ultimatesoftware.banking.customers.domain.models.CustomerAggregate;

public class CustomerCommandHandler extends CommandHandler<CustomerAggregate> {
    public CustomerCommandHandler(EventSourcingRepository repository, CommandBus commandBus) {
        super(repository, commandBus, CustomerAggregate.class);
    }
}
