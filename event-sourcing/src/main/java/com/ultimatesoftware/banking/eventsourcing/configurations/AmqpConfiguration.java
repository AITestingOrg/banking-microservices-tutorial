package com.ultimatesoftware.banking.eventsourcing.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class AmqpConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(AmqpConfiguration.class);

    @Value("${spring.data.mongodb.host}")
    String eventStoreHost;
    @Value("${spring.data.mongodb.port}")
    Integer eventStorePort;
    @Value("${spring.data.mongodb.database}")
    String eventStoreDatabase;
    @Value("${amqp.events.exchange-name}")
    String exchangeName;
    @Value("${amqp.events.queue-name}")
    String queueName;
    @Value("${amqp.events.host}")
    String amqpHost;
    @Value("${amqp.events.handlers}")
    String eventHandlerPackage;

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

    @Bean
    public ConnectionFactory connectionFactory(PropertiesConfiguration properties) {
        LOG.debug("amqpConnectionFactory() <- amqpHostName={}", properties.getAmqpHost());
        return new CachingConnectionFactory(properties.getAmqpHost());
    }
}
