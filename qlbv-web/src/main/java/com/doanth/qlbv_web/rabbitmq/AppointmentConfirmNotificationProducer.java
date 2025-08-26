package com.doanth.qlbv_web.rabbitmq;

import com.doanth.qlbv_web.dto.AppointmentInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class AppointmentConfirmNotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    public AppointmentConfirmNotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAppointmentConfirmMessage(AppointmentInfo message) {

//        String message = "hehe test rabbitmq";
        System.out.println("message gửi đi: " + message);
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_NOTIFICATION_APPOINTMENT_CONFIRM,
                message);
    }
    public void sendAppointmentNotConfirmMessage(AppointmentInfo message) {

//        String message = "hehe test rabbitmq";
        System.out.println("message gửi đi: " + message);
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_NOTIFICATION_APPOINTMENT_NOTCONFIRM,
                message);
    }
}
