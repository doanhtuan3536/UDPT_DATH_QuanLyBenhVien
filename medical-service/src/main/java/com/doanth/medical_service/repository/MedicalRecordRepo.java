package com.doanth.medical_service.repository;

import com.doanth.medical_service.dto.RecentPatientsDTO;
import com.doanth.medical_service.models.MedicalRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepo extends JpaRepository<MedicalRecord, Integer> {
    @Query("SELECT m FROM MedicalRecord m WHERE m.userId = ?1")
    public List<MedicalRecord> findByPatientId(Integer patientId);

    @Query("""
           SELECT new com.doanth.medical_service.dto.RecentPatientsDTO(m.userId, m.medicalRecordId)
           FROM MedicalRecord m
           WHERE m.doctorId = :doctorId
             AND NOT EXISTS (
                 SELECT 1 FROM MedicalRecord x
                 WHERE x.userId = m.userId
                   AND x.doctorId = :doctorId
                   AND (x.createdAt > m.createdAt
                        OR (x.createdAt = m.createdAt AND x.medicalRecordId > m.medicalRecordId))
             )
           ORDER BY m.createdAt DESC, m.medicalRecordId DESC
           """)
    List<RecentPatientsDTO> findRecentPatientsByDoctor(@Param("doctorId") int doctorId,
                                                       Pageable pageable);
}
