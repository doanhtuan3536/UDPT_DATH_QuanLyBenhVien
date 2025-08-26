package com.doanlt.notification_service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AppointmentConfirmNotificationListener {
    private final JavaMailSender mailSender;
    private final String emailFrom;

    public AppointmentConfirmNotificationListener(JavaMailSender mailSender, @Value("${spring.mail.username}") String emailFrom) {
        this.mailSender = mailSender;
        this.emailFrom = emailFrom;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_APPOINTMENT_CONFIRM_QUEUE)
    public void handleAppointmentConfirm(AppointmentInfo msg) {
        System.out.println("Nhận message user đã tạo: " + msg);
        String subject = "Appointment Confirm Notification";
//        String content = "Bạn vừa tạo mới user bệnh nhân với gmail đăng kí: " + msg.getEmail()
//                + " với username: " + msg.getUserName()+", tên đầy đủ: " + msg.getFullName() + " và số điên thoại là: " + msg.getSdt();
        String content = "Lịch khám của bạn vừa được xác nhận, lịch khám có id: " + msg.getAppointmentId() + ", ngày khám: " + msg.getAppointmentDate() + ", giờ khám: " + msg.getAppointmentTime()
                + ".\nLên website để xem thông tin chi tiết bác sĩ xác nhận lịch khám. Vui lòng đến trước 15 phút";
        String email = msg.getUserEmail();
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
    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_APPOINTMENT_NOTCONFIRM_QUEUE)
    public void handleAppointmentNotConfirm(AppointmentInfo msg) {
        System.out.println("Nhận message user đã tạo: " + msg);
        String subject = "Appointment Not Confirm Notification";
//        String content = "Bạn vừa tạo mới user bệnh nhân với gmail đăng kí: " + msg.getEmail()
//                + " với username: " + msg.getUserName()+", tên đầy đủ: " + msg.getFullName() + " và số điên thoại là: " + msg.getSdt();
        String content = "Lịch khám của bạn vừa bị hủy xác nhận bởi bác sĩ, lịch khám có id: " + msg.getAppointmentId() + ", ngày khám: " + msg.getAppointmentDate() + ", giờ khám: " + msg.getAppointmentTime()
                + ".\nLên website để xem thông tin chi tiết.";
        String email = msg.getUserEmail();
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
