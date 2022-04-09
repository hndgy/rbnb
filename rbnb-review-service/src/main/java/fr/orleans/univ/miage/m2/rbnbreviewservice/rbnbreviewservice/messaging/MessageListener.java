package fr.orleans.univ.miage.m2.rbnbreviewservice.rbnbreviewservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @RabbitListener(queues = ConfigMQ.REVIEW)
    public void receiver(String message){
        System.out.println(message);
    }

}
