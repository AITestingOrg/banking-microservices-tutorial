package com.ultimatesoftware.banking.eventsourcing.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * AmqpConfiguration is the base type for AMQP based subscriber and publisher configurations.
 * This class exist to share the ConnectionFactory Bean and provide initial configuration values
 * todo: implement a more elegant configuration scheme.
 */
@Component
public class AmqpConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(AmqpConfiguration.class);

    @Value("${spring.data.mongodb.host}")
    private String eventStoreHost;
    @Value("${spring.data.mongodb.port}")
    private Integer eventStorePort;
    @Value("${spring.data.mongodb.database}")
    private String eventStoreDatabase;
    @Value("${amqp.events.exchange-name}")
    private String exchangeName;
    @Value("${amqp.events.queue-name}")
    private String queueName;
    @Value("${amqp.events.host}")
    private String amqpHost;
    @Value("${amqp.events.handlers}")
    private String eventHandlerPackage;

    /**
     * Creates the PropertiesConfiguration instance with constant configuration values
     * this is only necessary due to Annnotation expressions requirement of constants.
     * All AMQP configuration and EventStore configuration values should be here for consistency.
     * @return PropertiesConfiguration instance
     */
    @Bean
    public PropertiesConfiguration properties() {
        return new PropertiesConfiguration.PropertiesBuilder()
                .setAmqpHost(amqpHost)
                .setEventHandlerPackage(eventHandlerPackage)
                .setEventStoreDatabase(eventStoreDatabase)
                .setEventStoreHost(eventStoreHost)
                .setEventStorePort(eventStorePort)
                .setExchangeName(exchangeName)
                .setQueueName(queueName)
                .build();
    }

    /**
     * AMQP Caching Connection Factory Bean to build RabbitMQ connections. Utilizes the
     * AMQPHost configuration value.
     * @param properties PropertiesConfiguration
     * @return RabbitMQ AMQP connection factory configured with a host.
     */
    @Bean
    public ConnectionFactory connectionFactory(PropertiesConfiguration properties) {
        LOG.debug("amqpConnectionFactory() <- amqpHostName={}", properties.getAmqpHost());
        return new CachingConnectionFactory(properties.getAmqpHost());
    }
}
