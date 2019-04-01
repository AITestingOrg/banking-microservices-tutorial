package com.ultimatesoftware.banking.api.configuration;

import com.ultimatesoftware.banking.api.operations.AxonEventHandler;
import io.micronaut.context.annotation.Requires;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Requires(notEnv = ConfigurationConstants.EXTERNAL_MOCKS)
@Requires(notEnv = ConfigurationConstants.INTERNAL_MOCKS)
public class SagaManagerConfiguration<T> {
    protected static final Logger LOG = LoggerFactory.getLogger(AxonEventHandler.class);
    private Class<T> type;
    private Configuration configurer;
    private final AxonServerConfiguration axonServerConfiguration;
    private final CommandGateway commandGateway;

    public SagaManagerConfiguration(AxonServerConfiguration axonServerConfiguration, CommandGateway commandGateway, Class<T> type) {
        this.axonServerConfiguration = axonServerConfiguration;
        this.type = type;
        this.commandGateway = commandGateway;
    }

    public void configure() {
        LOG.info("Configuring Axon server for Saga Manager");
        configurer = DefaultConfigurer.defaultConfiguration()
            .registerComponent(AxonServerConfiguration.class, c -> axonServerConfiguration)
            .registerComponent(CommandGateway.class, c -> commandGateway)
            .eventProcessing(eventProcessingConfigurer -> eventProcessingConfigurer
                .registerSaga(type)).start();
        LOG.info("Axon Saga Manager on service started");
    }
}
