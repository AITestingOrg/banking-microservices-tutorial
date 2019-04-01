package com.ultimatesoftware.banking.account.cmd.configuration;

import com.ultimatesoftware.banking.account.cmd.sagas.TransactionSaga;
import com.ultimatesoftware.banking.api.configuration.SagaManagerConfiguration;
import io.micronaut.context.annotation.Infrastructure;
import io.micronaut.discovery.event.ServiceStartedEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.commandhandling.gateway.CommandGateway;

@Infrastructure
public class AccountSagaManagerConfiguration extends SagaManagerConfiguration<TransactionSaga> {
    public AccountSagaManagerConfiguration(
        AxonServerConfiguration axonServerConfiguration, CommandGateway commandGateway) {
        super(axonServerConfiguration, commandGateway, TransactionSaga.class);
    }

    @EventListener
    public void configure(ServiceStartedEvent event) {
        super.configure();
    }
}
