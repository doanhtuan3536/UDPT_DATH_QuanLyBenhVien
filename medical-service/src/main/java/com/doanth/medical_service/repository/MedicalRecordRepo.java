package com.doanth.medical_service.repository;

import com.doanth.medical_service.models.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepo extends JpaRepository<MedicalRecord, Integer> {
    @Query("SELECT m FROM MedicalRecord m WHERE m.userId = ?1")
    public List<MedicalRecord> findByPatientId(Integer patientId);
}
