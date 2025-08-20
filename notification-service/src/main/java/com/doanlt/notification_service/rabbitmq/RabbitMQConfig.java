package com.doanlt.notification_service.rabbitmq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
//    public static final String EXCHANGE = "article.exchange";
////    public static final String QUEUE = "article.create.queue";
//    public static final String ROUTING_KEY_RESULT = "article.result";
//    public static final String RESULT_QUEUE = "article.result.queue";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_USER_CREATED_QUEUE = "notification.user.created.queue";
//    public static final String NOTIFICATION_ADD_MEMBER_RESULT_QUEUE = "notification.add.member.result.queue";
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE);
//    }
//    @Bean
//    public TopicExchange notificationExchange() {
//        return new TopicExchange(NOTIFICATION_EXCHANGE);
//    }
//    @Bean
//    public Queue notificationArticleCreateResultQueue() {
//        return new Queue(NOTIFICATION_ARTICLE_CREATE_RESULT_QUEUE);
//    }
//
////    @Bean
////    public Binding binding() {
////        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
////    }
//
//    @Bean
//    public Binding articleCreateResultBinding() {
//        return BindingBuilder.bind(notificationArticleCreateResultQueue())
//                .to(notificationExchange())
//                .with(ROUTING_KEY_ARTICLE_CREATE_RESULT_NOTIFICATION);
//    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
