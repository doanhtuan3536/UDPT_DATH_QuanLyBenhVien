package com.doanlt.notification_service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedNotificationListener {
    private final JavaMailSender mailSender;
    private final String emailFrom;

    public UserCreatedNotificationListener(JavaMailSender mailSender, @Value("${spring.mail.username}") String emailFrom) {
        this.mailSender = mailSender;
        this.emailFrom = emailFrom;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_USER_CREATED_QUEUE)
    public void handleUserCreated(SignupResult msg) {
        System.out.println("Nhận message user đã tạo: " + msg);
        String subject = "New User Created Notification";
        String content = "Bạn vừa tạo mới user bệnh nhân với gmail đăng kí: " + msg.getEmail()
                + " với username: " + msg.getUserName()+", tên đầy đủ: " + msg.getFullName() + " và số điên thoại là: " + msg.getSdt();
        String email = msg.getEmail();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(email);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + email);

        } catch (MailAuthenticationException e) {
            System.err.println("Authentication failed for email: " + email);
        } catch (MailSendException e) {
            System.err.println("Failed to send email to: " + email);
            if (e.getFailedMessages() != null) {
                e.getFailedMessages().forEach((k, v) ->
                        System.err.println("Failed message details: " + k + " - " + v));
            }

        } catch (Exception e) {
            System.err.println("Unexpected error when sending to: " + email);
            e.printStackTrace();
        }

    }

}
