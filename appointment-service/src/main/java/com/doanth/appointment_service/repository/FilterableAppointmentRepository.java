package com.doanth.appointment_service.repository;

import com.doanth.appointment_service.models.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface FilterableAppointmentRepository {
    public Page<Appointment> listWithFilter(Pageable pageable, Map<String, Object> filterFields);
}
