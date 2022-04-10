package fr.orleans.univ.miage.m2.rbnbreservationservice.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigMQ {

    public static final String REVIEW = "review";
    public static final String RBNB_UTILISATEURS_REMOVE = "rbnb_utilisateurs_remove";

    @Bean
    public Queue userRemoveQueue(){
            return new Queue(REVIEW);
        }

    @Bean
    public FanoutExchange removeExchange(){return new FanoutExchange(RBNB_UTILISATEURS_REMOVE);}

    @Bean
    public Binding reviewRemoveBinding(Queue queue, FanoutExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange);
    }

    @Bean
    public MessageConverter messageConverter(){
            return new Jackson2JsonMessageConverter();
        }


    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}

