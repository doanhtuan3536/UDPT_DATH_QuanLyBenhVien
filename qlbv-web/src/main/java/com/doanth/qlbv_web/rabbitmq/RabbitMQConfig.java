package com.doanth.qlbv_web.rabbitmq;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.*;
@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_USER_CREATED_QUEUE = "notification.user.created.queue";
    public static final String ROUTING_KEY_NOTIFICATION_USER_CREATED = "notification.user.created";



    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationUserCreatedqueue() {
        return new Queue(NOTIFICATION_USER_CREATED_QUEUE);
    }

    @Bean
    public Binding notificationUserCreatedBinding() {
        return BindingBuilder.bind(notificationUserCreatedqueue())
                .to(notificationExchange())
                .with(ROUTING_KEY_NOTIFICATION_USER_CREATED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
