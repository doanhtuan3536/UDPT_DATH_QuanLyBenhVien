package com.doanth.medical_service.repository;

import com.doanth.medical_service.models.PrecriptionDetailId;
import com.doanth.medical_service.models.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionDetailRepo extends JpaRepository<PrescriptionDetail, PrecriptionDetailId> {

    @Query("SELECT p FROM PrescriptionDetail p WHERE p.precriptionDetailId.prescriptionId.patientId = ?1 AND p.precriptionDetailId.prescriptionId.examinationId = ?2")
    public List<PrescriptionDetail> findByPatientIdAndExaminationId(Integer patientId, Integer examinationId);
}
