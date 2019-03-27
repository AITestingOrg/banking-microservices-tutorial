package com.ultimatesoftware.banking.account.cmd.configuration;

import com.ultimatesoftware.banking.account.cmd.sagas.TransactionSaga;
import com.ultimatesoftware.banking.api.operations.SagaManagerConfiguration;
import io.micronaut.context.annotation.Infrastructure;
import org.axonframework.axonserver.connector.AxonServerConfiguration;

@Infrastructure
public class AccountSagaManagerConfiguration extends SagaManagerConfiguration<TransactionSaga> {
    public AccountSagaManagerConfiguration(
        AxonServerConfiguration axonServerConfiguration) {
        super(axonServerConfiguration, TransactionSaga.class);
    }
}
