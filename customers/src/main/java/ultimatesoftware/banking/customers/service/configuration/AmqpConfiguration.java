package ultimatesoftware.banking.customers.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(AmqpConfiguration.class);

    @Value("${amqp.events.host}")
    protected String hostName;


    @Bean
    public ConnectionFactory connectionFactory() {

        LOG.debug("amqpConnectionFactory() <- amqpHostName={}", hostName);

        return new CachingConnectionFactory(hostName);
    }
}
