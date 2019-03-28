package com.ultimatesoftware.banking.api.operations;

import com.ultimatesoftware.banking.api.configuration.ConfigurationConstants;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Requires(notEnv = ConfigurationConstants.EXTERNAL_MOCKS)
@Requires(notEnv = ConfigurationConstants.INTERNAL_MOCKS)
public class AxonEventHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(AxonEventHandler.class);
    private Configuration configurer;
    private final AxonServerConfiguration axonServerConfiguration;

    public AxonEventHandler(AxonServerConfiguration axonServerConfiguration) {
        this.axonServerConfiguration = axonServerConfiguration;
        LOG.info("Configuring Axon server");
    }

    public void configure(ServerStartupEvent event) {
        configurer = DefaultConfigurer.defaultConfiguration()
            .registerComponent(AxonServerConfiguration.class, c -> axonServerConfiguration)
            .eventProcessing(eventProcessingConfigurer -> eventProcessingConfigurer
                .registerEventHandler(conf -> this)).start();
        LOG.info("Event handler on service started");
    }
}
