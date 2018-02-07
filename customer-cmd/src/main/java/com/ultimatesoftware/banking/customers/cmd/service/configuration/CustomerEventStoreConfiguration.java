package com.ultimatesoftware.banking.customers.cmd.service.configuration;

import com.ultimatesoftware.banking.customers.cmd.domain.aggregates.Customer;
import com.ultimatesoftware.banking.customers.cmd.domain.CustomerCommandHandler;
import com.ultimatesoftware.banking.eventsourcing.configurations.AmqpEventPublisherConfiguration;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerEventStoreConfiguration extends AmqpEventPublisherConfiguration<Customer, CustomerCommandHandler> {
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
