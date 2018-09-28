package com.ultimatesoftware.banking.eventsourcing.handlers;

import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CustomCommandHandler provides a pattern and default implementation for subscribing the command
 * handlers.
 * @param <T>
 */
public abstract class CustomCommandHandler<T> {
    protected AggregateAnnotationCommandHandler<T> handler;
    protected CommandBus commandBus;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * By default builds a AggregateAnnotationCommandHandler from the commandBus, Aggregate type, and repository.
     * @param repository EventSource Repository
     * @param commandBus CommandBus
     * @param type Aggregate Type
     */
    public CustomCommandHandler(EventSourcingRepository<T> repository, CommandBus commandBus, Class<T> type) {
        log.info("Creating Command Handler.");
         handler = new AggregateAnnotationCommandHandler<T>(type, repository);
         this.commandBus = commandBus;
        log.info("Successfully created Command Handler.");
    }

    /**
     * Subscribe the handlers to the commandBus
     */
    public void subscribe() {
        handler.subscribe(commandBus);
        log.info("Handler subscribed to Command Bus");
    }
}
