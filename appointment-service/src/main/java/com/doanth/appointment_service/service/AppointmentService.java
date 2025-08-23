package com.doanth.appointment_service.service;

import com.doanth.appointment_service.models.Appointment;
import com.doanth.appointment_service.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    private final AppointmentRepository repo;

    public AppointmentService(AppointmentRepository repo) {
        this.repo = repo;
    }

    public Appointment add(Appointment appointment) {
        return repo.save(appointment);
    }
}
