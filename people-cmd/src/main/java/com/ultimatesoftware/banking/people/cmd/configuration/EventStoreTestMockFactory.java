package com.ultimatesoftware.banking.people.cmd.configuration;

import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import com.ultimatesoftware.banking.api.test.TestMockCommandGateway;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import javax.inject.Singleton;
import org.axonframework.commandhandling.gateway.CommandGateway;

@Factory
@Requires(env = ConfigurationConstants.INTERNAL_MOCKS)
public class EventStoreTestMockFactory {
    @Bean
    @Singleton
    @Primary
    public CommandGateway commandGateway() {
        return new TestMockCommandGateway();
    }
}
