package com.doanth.qlbv_web.rabbitmq;

import com.doanth.qlbv_web.dto.SignupResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedNotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    public UserCreatedNotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUserCreatedMessage(SignupResult message) {

//        String message = "hehe test rabbitmq";
        System.out.println("message gửi đi: " + message);
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_NOTIFICATION_USER_CREATED,
                message);
    }
}
