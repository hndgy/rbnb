package fr.orleans.univ.miage.m2.rbnbreservationservice.configuration;
/*
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String RESERVATION_QUEUE = "reservation_queue";
    public static final String RESERVATION_EXCHANGE = "reservation_exchange";
    public static final String RESERVATION_ROUTING_KEY = "reservation_routingKey";
    public static final String MAILING_EXCHANGE = "rbnb_mailing";
    public static final String MAILING_ROUTING_KEY = "mailing.info";

    @Bean
    public Queue logementQueue() {
        return new Queue(RESERVATION_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RESERVATION_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(RESERVATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}

 */
