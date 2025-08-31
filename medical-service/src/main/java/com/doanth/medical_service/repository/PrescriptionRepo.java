package com.doanth.medical_service.repository;

import com.doanth.medical_service.models.Prescription;
import com.doanth.medical_service.models.PrescriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrescriptionRepo extends JpaRepository<Prescription, PrescriptionId> {
    @Query("SELECT p FROM Prescription p WHERE p.prescriptionId.patientId = ?1 AND p.prescriptionId.examinationId = ?2")
    public Prescription findByPatientIdAndExaminationId(Integer patientId, Integer examinationId);
}
