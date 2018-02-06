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

public class AmqpEventConfiguration {

    protected static final Logger LOG = LoggerFactory.getLogger(AmqpEventConfiguration.class);

    @Value("${amqp.events.exchange-name}")
    protected String exchangeName;
    @Value("${amqp.events.queue-name}")
    protected String queueName;
    @Value("${amqp.events.handlers")
    protected String eventHandlerPackage;

    @Bean
    public AmqpAdmin eventAdmin(ConnectionFactory connectionFactory) {

        LOG.debug("EventAdmin(connectionFactory={})", connectionFactory);

        RabbitAdmin admin;
        admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(eventExchange());
        admin.declareQueue(eventQueue());
        admin.declareBinding(eventBinding());
        return admin;
    }

    @Bean
    public FanoutExchange eventExchange() {

        LOG.debug("EventExchange() <- getAmqpEventsInExchangeName={}", exchangeName);

        FanoutExchange exchange = new FanoutExchange(exchangeName, true, false);
        return exchange;
    }

    @Bean
    public Queue eventQueue() {

        LOG.debug("EventQueue() <- getAmqpEventsInQueueName={}", queueName);

        Queue queue = new Queue(queueName, false, false, true);
        return queue;
    }

    @Bean
    public Binding eventBinding() {

        LOG.debug("EventBinding() <- queueName={}, exchangeName={}",
                exchangeName, exchangeName);

        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE,
                exchangeName, "", null);
        return binding;
    }

    @Bean
    public SpringAMQPMessageSource eventMessageSource(Serializer serializer) {
        return  new SpringAMQPMessageSource(serializer) {
            @RabbitListener(queues = "eventQueue-customers")
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                super.onMessage(message, channel);
            }
        };
    }

    @Autowired
    public void registerEventProcessors(EventHandlingConfiguration config, SpringAMQPMessageSource source) {
        config.registerSubscribingEventProcessor(eventHandlerPackage, c -> source);
    }
}
