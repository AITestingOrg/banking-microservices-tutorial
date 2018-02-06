package com.ultimatesoftware.banking.eventsourcing.configurations;

import com.rabbitmq.client.Channel;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

public class AmqpEventSubscriptionConfiguration extends AmqpConfiguration {

    protected static final Logger LOG = LoggerFactory.getLogger(AmqpEventSubscriptionConfiguration.class);

    @Bean
    public AmqpAdmin eventAdmin(ConnectionFactory connectionFactory, PropertiesConfiguration properties) {
        LOG.debug("eventAdmin(connectionFactory={})", connectionFactory);
        RabbitAdmin admin;
        admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(eventExchange(properties));
        admin.declareQueue(eventQueue(properties));
        admin.declareBinding(eventBinding(properties));
        return admin;
    }

    @Bean
    public FanoutExchange eventExchange(PropertiesConfiguration properties) {

        LOG.debug("EventExchange() <- getAmqpEventsInExchangeName={}", properties.getExchangeName());

        FanoutExchange exchange = new FanoutExchange(properties.getExchangeName(), true, false);
        return exchange;
    }

    @Bean
    public Queue eventQueue(PropertiesConfiguration properties) {

        LOG.debug("EventQueue() <- getAmqpEventsInQueueName={}", properties.getQueueName());

        Queue queue = new Queue(properties.getQueueName(), false, false, true);
        return queue;
    }

    @Bean
    public Binding eventBinding(PropertiesConfiguration properties) {

        LOG.debug("EventBinding() <- queueName={}, exchangeName={}",
                properties.getQueueName(), properties.getExchangeName());

        Binding binding = new Binding(properties.getQueueName(), Binding.DestinationType.QUEUE,
                properties.getExchangeName(), "", null);
        return binding;
    }

    @Bean
    public SpringAMQPMessageSource eventMessageSource(Serializer serializer) {
        return  new SpringAMQPMessageSource(serializer) {
            @RabbitListener(queues = "#{@properties.queueName}")
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                super.onMessage(message, channel);
            }
        };
    }

    @Autowired
    public void registerEventProcessors(EventHandlingConfiguration config, SpringAMQPMessageSource source, PropertiesConfiguration properties) {
        config.registerSubscribingEventProcessor(properties.getEventHandlerPackage(), c -> source);
    }
}
