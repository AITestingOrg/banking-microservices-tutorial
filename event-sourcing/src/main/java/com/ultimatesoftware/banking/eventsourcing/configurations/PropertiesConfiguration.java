package com.ultimatesoftware.banking.eventsourcing.configurations;

/**
 * Stores the current configuration in constants for use with annotation expressions.
 */
public class PropertiesConfiguration {
    private final String eventStoreHost;
    private final Integer eventStorePort;
    private final String eventStoreDatabase;
    private final String exchangeName;
    private final String queueName;
    private final String eventHandlerPackage;
    private final String amqpHost;

    private PropertiesConfiguration(String eventStoreHost, Integer eventStorePort, String eventStoreDatabase, String exchangeName, String queueName, String eventHandlerPackage, String amqpHost) {
        this.eventStoreHost = eventStoreHost;
        this.eventStorePort = eventStorePort;
        this.eventStoreDatabase = eventStoreDatabase;
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.eventHandlerPackage = eventHandlerPackage;
        this.amqpHost = amqpHost;
    }

    /**
     * Returns the EventStore Database host
     * @return the EventStore Database host
     */
    public String getEventStoreHost() {
        return eventStoreHost;
    }

    /**
     * Returns the EventStore Database port
     * @return the EventStore Database port
     */
    public Integer getEventStorePort() {
        return eventStorePort;
    }

    /**
     * Returns the EventStore Database name
     * @return the EventStore Database name
     */
    public String getEventStoreDatabase() {
        return eventStoreDatabase;
    }

    /**
     * Returns the EventBus exchange name
     * @return the EventBus exchange name
     */
    public String getExchangeName() {
        return exchangeName;
    }

    /**
     * Returns the EventBus queue name
     * @return the EventBus queue name
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * Returns the package containing the event handlers
     * @return the package containing the event handlers
     */
    public String getEventHandlerPackage() {
        return eventHandlerPackage;
    }

    /**
     * Returns the RabbitMQ host
     * @return the RabbitMQ host
     */
    public String getAmqpHost() {
        return amqpHost;
    }

    /**
     * Builder for creating PropertyConfigurations.
     */
    public static class PropertiesBuilder {
        private String eventStoreHost;
        private Integer eventStorePort;
        private String eventStoreDatabase;
        private String exchangeName;
        private String queueName;
        private String eventHandlerPackage;
        private String amqpHost;

        public PropertiesBuilder setEventStoreHost(String eventStoreHost) {
            this.eventStoreHost = eventStoreHost;
            return this;
        }

        public PropertiesBuilder setEventStorePort(Integer eventStorePort) {
            this.eventStorePort = eventStorePort;
            return this;
        }

        public PropertiesBuilder setEventStoreDatabase(String eventStoreDatabase) {
            this.eventStoreDatabase = eventStoreDatabase;
            return this;
        }

        public PropertiesBuilder setExchangeName(String exchangeName) {
            this.exchangeName = exchangeName;
            return this;
        }


        public PropertiesBuilder setQueueName(String queueName) {
            this.queueName = queueName;
            return this;
        }


        public PropertiesBuilder setEventHandlerPackage(String eventHandlerPackage) {
            this.eventHandlerPackage = eventHandlerPackage;
            return this;
        }

        public PropertiesBuilder setAmqpHost(String amqpHost) {
            this.amqpHost = amqpHost;
            return this;
        }

        public PropertiesConfiguration build() {
            return new PropertiesConfiguration(eventStoreHost, eventStorePort, eventStoreDatabase, exchangeName, queueName, eventHandlerPackage, amqpHost);
        }
    }
}
