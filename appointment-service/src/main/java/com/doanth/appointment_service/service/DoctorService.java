package com.doanth.appointment_service.service;

import com.doanth.appointment_service.repository.DoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    private final DoctorRepository repo;

    public DoctorService(DoctorRepository repo) {
        this.repo = repo;
    }

    public Integer getSpecialtyIdByDoctorId(Integer doctorId) {
        return repo.findSpecialtyIdByDoctorId(doctorId);
    }
}
