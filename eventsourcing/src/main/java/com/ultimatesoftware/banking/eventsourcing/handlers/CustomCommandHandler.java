package com.ultimatesoftware.banking.eventsourcing.handlers;

import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;

/**
 * CustomCommandHandler provides a pattern and default implementation for subscribing the command
 * handlers.
 * @param <T>
 */
public abstract class CustomCommandHandler<T> {
    protected AggregateAnnotationCommandHandler<T> handler;
    protected CommandBus commandBus;

    /**
     * By default builds a AggregateAnnotationCommandHandler from the commandBus, Aggregate type, and repository.
     * @param repository EventSource Repository
     * @param commandBus CommandBus
     * @param type Aggregate Type
     */
    public CustomCommandHandler(EventSourcingRepository repository, CommandBus commandBus, Class<T> type) {
         handler = new AggregateAnnotationCommandHandler<T>(type, repository);
         this.commandBus = commandBus;
    }

    /**
     * Subscribe the handlers to the commandBus
     */
    public void subscribe() {
        handler.subscribe(commandBus);
    }
}
