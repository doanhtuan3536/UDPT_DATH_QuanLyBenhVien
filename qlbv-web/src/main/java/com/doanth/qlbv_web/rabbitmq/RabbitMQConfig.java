package com.doanth.qlbv_web.rabbitmq;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.*;

import java.time.LocalTime;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_USER_CREATED_QUEUE = "notification.user.created.queue";
    public static final String ROUTING_KEY_NOTIFICATION_USER_CREATED = "notification.user.created";

    public static final String NOTIFICATION_APPOINTMENT_CONFIRM_QUEUE = "notification.appointment.confirm.queue";
    public static final String ROUTING_KEY_NOTIFICATION_APPOINTMENT_CONFIRM = "notification.appointment.confirm";
    public static final String NOTIFICATION_APPOINTMENT_NOTCONFIRM_QUEUE = "notification.appointment.notconfirm.queue";
    public static final String ROUTING_KEY_NOTIFICATION_APPOINTMENT_NOTCONFIRM = "notification.appointment.notconfirm";



    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationUserCreatedqueue() {
        return new Queue(NOTIFICATION_USER_CREATED_QUEUE);
    }

    @Bean
    public Queue notificationAppointmentConfirmQueue() {
        return new Queue(NOTIFICATION_APPOINTMENT_CONFIRM_QUEUE);
    }
    @Bean
    public Queue notificationAppointmentNotConfirmQueue() {
        return new Queue(NOTIFICATION_APPOINTMENT_NOTCONFIRM_QUEUE);
    }

    @Bean
    public Binding notificationUserCreatedBinding() {
        return BindingBuilder.bind(notificationUserCreatedqueue())
                .to(notificationExchange())
                .with(ROUTING_KEY_NOTIFICATION_USER_CREATED);
    }

    @Bean
    public Binding notificationAppointmentConfirmBinding() {
        return BindingBuilder.bind(notificationAppointmentConfirmQueue())
                .to(notificationExchange())
                .with(ROUTING_KEY_NOTIFICATION_APPOINTMENT_CONFIRM);
    }
    @Bean
    public Binding notificationAppointmentNotConfirmBinding() {
        return BindingBuilder.bind(notificationAppointmentNotConfirmQueue())
                .to(notificationExchange())
                .with(ROUTING_KEY_NOTIFICATION_APPOINTMENT_NOTCONFIRM);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static void main(String[] args) {
        System.out.println(LocalTime.of(16, 0));
    }
}
