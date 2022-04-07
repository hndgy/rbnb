package fr.orleans.univ.miage.m2.rbnbmailingservice.consumers;

import fr.orleans.univ.miage.m2.rbnbmailingservice.services.MailingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotifConsumer {

    private final MailingService mailingService;

    public NotifConsumer(MailingService mailingService) {
        this.mailingService = mailingService;
    }

    @RabbitListener(queues = "info")
    public void mailUserListener(EmailMessage message){
        mailingService.sendMail(message.getToEmail(), message.getSubject(), message.getContent());
    }

}
