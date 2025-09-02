package com.doanth.medical_service.service;

import com.doanth.medical_service.dto.RecentPatientsDTO;
import com.doanth.medical_service.repository.ExaminationRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExaminationService {
    private final ExaminationRepo examinationRepo;

    public ExaminationService(ExaminationRepo examinationRepo) {
        this.examinationRepo = examinationRepo;
    }

    public List<RecentPatientsDTO> getRecentExaminationsCreatedPatientsByDoctorId(int doctorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return examinationRepo.findTopRecentExaminationPatientsByDoctorId(doctorId, pageable);
    }


}
