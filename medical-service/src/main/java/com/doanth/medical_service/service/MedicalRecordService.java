package com.doanth.medical_service.service;

import com.doanth.medical_service.dto.RecentPatientsDTO;
import com.doanth.medical_service.models.MedicalRecord;
import com.doanth.medical_service.repository.MedicalRecordRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepo medicalRecordRepo;

    public MedicalRecordService(MedicalRecordRepo medicalRecordRepo) {
        this.medicalRecordRepo = medicalRecordRepo;
    }

    public MedicalRecord getMedicalRecord(int medicalRecordId) {
        return medicalRecordRepo.findById(medicalRecordId).orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found"));
    }
    public MedicalRecord add(MedicalRecord medicalRecord) {
        return medicalRecordRepo.save(medicalRecord);
    }

    public List<MedicalRecord> listByPatientId(int patientId) {
        List<MedicalRecord> medicalRecords = medicalRecordRepo.findByPatientId(patientId);
        if(medicalRecords == null) throw new MedicalRecordNotFoundException("Medical record not found");
        return medicalRecords;
    }

    public List<RecentPatientsDTO> getRecentMedicalRecordsCreatedPatientsByDoctorId(int doctorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return medicalRecordRepo.findRecentPatientsByDoctor(doctorId, pageable);
    }
}
