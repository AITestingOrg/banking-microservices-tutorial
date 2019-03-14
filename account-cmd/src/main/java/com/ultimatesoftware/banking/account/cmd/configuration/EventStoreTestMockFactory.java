package com.ultimatesoftware.banking.account.cmd.configuration;

import com.ultimatesoftware.banking.account.cmd.aggregates.Account;
import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.factories.AxonEventStoreFactory;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.Registration;
import org.axonframework.messaging.MessageDispatchInterceptor;

@Factory
@Requires(env = ConfigurationConstants.INTERNAL_MOCKS)
public class EventStoreTestMockFactory extends AxonEventStoreFactory<Account> {
    public EventStoreTestMockFactory(
        Class<Account> type) {
        super(type);
    }

    @Bean
    @Singleton
    public CommandGateway commandGateway() {
        return new CommandGateway() {
            @Override
            public <C, R> void send(C command, CommandCallback<? super C, ? super R> callback) {

            }

            @Override public <R> R sendAndWait(Object command) {
                return null;
            }

            @Override public <R> R sendAndWait(Object command, long timeout, TimeUnit unit) {
                return null;
            }

            @Override public <R> CompletableFuture<R> send(Object command) {
                return null;
            }

            @Override public Registration registerDispatchInterceptor(
                MessageDispatchInterceptor<? super CommandMessage<?>> dispatchInterceptor) {
                return null;
            }
        };
    }
}
