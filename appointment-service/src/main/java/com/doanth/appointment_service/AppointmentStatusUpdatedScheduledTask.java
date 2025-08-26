package com.doanth.appointment_service;

import com.doanth.appointment_service.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class AppointmentStatusUpdatedScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentStatusUpdatedScheduledTask.class);
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Scheduled(fixedDelayString = "${service.appointment.status.updated.interval}", initialDelay = 2000)
    @Transactional
    public void deleteExpiredRefreshTokens() {
        int appointmentUpdated = appointmentRepository.updateStatusExpired();

        LOGGER.info("Number of appointment expired: " + appointmentUpdated);

    }
}
