package fr.orleans.univ.miage.m2.rbnblogementservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String LOGEMENT_QUEUE = "logement_queue";
    public static final String LOGEMENT_EXCHANGE = "logement_exchange";
    public static final String LOGEMENT_ROUTING_KEY = "logement_routingKey";
    public static final String MAILING_EXCHANGE = "rbnb_mailing";
    public static final String MAILING_ROUTING_KEY = "mailing.info";

    @Bean
    public Queue logementQueue() {
        return new Queue(LOGEMENT_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(LOGEMENT_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(LOGEMENT_ROUTING_KEY);
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
