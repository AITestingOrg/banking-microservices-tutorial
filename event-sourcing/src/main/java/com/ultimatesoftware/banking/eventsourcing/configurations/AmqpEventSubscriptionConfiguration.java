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
import org.springframework.context.annotation.Bean;

/**
 * The AMQP Event Subscription Configuration binds the event handlers to an AMQP event queue for a configured
 * exchange.
 */
public class AmqpEventSubscriptionConfiguration extends AmqpConfiguration {

    protected static final Logger LOG = LoggerFactory.getLogger(AmqpEventSubscriptionConfiguration.class);

    /**
     * Configures the AMQPAdmin or portable administration options for RabbitMQ.
     * @param connectionFactory Provided ConnectionFactory
     * @param properties Provided PropertiesConfiguration
     * @return the configured AMQPAdmin
     */
    @Bean
    public AmqpAdmin eventAdmin(ConnectionFactory connectionFactory, PropertiesConfiguration properties) {
        LOG.debug("eventAdmin(connectionFactory={})", connectionFactory);
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(eventExchange(properties));
        admin.declareQueue(eventQueue(properties));
        admin.declareBinding(eventBinding(properties));
        return admin;
    }

    /**
     * Configures a fanout Exchange so that all events received are sent to all known queues.
     * @param properties Provided PropertiesConfiguration
     * @return configured fanout Exchange
     */
    @Bean
    public FanoutExchange eventExchange(PropertiesConfiguration properties) {

        LOG.debug("EventExchange() <- getAmqpEventsInExchangeName={}", properties.getExchangeName());

        FanoutExchange exchange = new FanoutExchange(properties.getExchangeName(), true, false);
        return exchange;
    }

    /**
     * Configures the event queue.
     * @param properties Provided PropertiesConfiguration
     * @return The configured event queue.
     */
    @Bean
    public Queue eventQueue(PropertiesConfiguration properties) {

        LOG.debug("EventQueue() <- getAmqpEventsInQueueName={}", properties.getQueueName());

        Queue queue = new Queue(properties.getQueueName(), false, false, true);
        return queue;
    }

    /**
     * Binds the queue to the exchange.
     * @param properties PropertiesConfiguration
     * @return Binding for AMQP
     */
    @Bean
    public Binding eventBinding(PropertiesConfiguration properties) {

        LOG.debug("EventBinding() <- queueName={}, exchangeName={}",
                properties.getQueueName(), properties.getExchangeName());

        Binding binding = new Binding(properties.getQueueName(), Binding.DestinationType.QUEUE,
                properties.getExchangeName(), "", null);
        return binding;
    }

    /**
     * Configures the event lister for the provided queue. The SpringAMQPMessageSource
     * will forward events to whatever the EventHandlingConfiguration dictates.
     * @param serializer Provided serializer, defaults to Jackson
     * @return returns the configured SpringAMQPMessageSource
     * todo: if the expression can be implemented differently on the RabbitMQ listener then
     * the PropertiesConfiguration class can be removed.
     */
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

    /**
     * Registers the event handler's package name for scanning @EventHandler annotations to be bound
     * to the SpringAMQPMessageSource.
     * @param config Provided EventHandlingConfiguration
     * @param source Configured SpringAMQPMessageSource
     * @param properties Provided PropertiesConfiguration
     */
    @Autowired
    public void registerEventProcessors(EventHandlingConfiguration config, SpringAMQPMessageSource source, PropertiesConfiguration properties) {
        config.registerSubscribingEventProcessor(properties.getEventHandlerPackage(), c -> source);
    }
}
