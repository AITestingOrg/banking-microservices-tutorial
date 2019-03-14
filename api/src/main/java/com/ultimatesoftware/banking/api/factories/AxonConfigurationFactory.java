package com.ultimatesoftware.banking.api.factories;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import javax.inject.Singleton;
import org.axonframework.axonserver.connector.AxonServerConfiguration;

@Factory
@Requires(property = "micronaut.application.name")
@Requires(property = "axon.axonserver.servers")
public class AxonConfigurationFactory {
    @Value("${micronaut.application.name}")
    private String appName;
    @Value("${axon.axonserver.servers}")
    private String axonServerHost;

    @Primary
    @Bean
    @Singleton
    public AxonServerConfiguration axonServerConfiguration() {
        return AxonServerConfiguration.builder()
            .componentName(appName)
            .servers(axonServerHost)
            .build();
    }
}
