package com.ultimatesoftware.banking.api.factories;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import javax.inject.Singleton;
import org.axonframework.axonserver.connector.AxonServerConfiguration;

@Factory
public class AxonConfigurationFactory {
    @Value("${micronaut.application.name}")
    private String appName;
    @Value("${axon.axonserver.servers}")
    private String axonServerHost;

    @Bean
    @Singleton
    public AxonServerConfiguration axonServerConfiguration() {
        return AxonServerConfiguration.builder()
            .componentName(appName)
            .servers(axonServerHost)
            .build();
    }
}
