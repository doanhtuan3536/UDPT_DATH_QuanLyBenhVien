package com.doanth.medical_service.service;

import com.doanth.medical_service.models.Prescription;
import com.doanth.medical_service.models.PrescriptionDetail;
import com.doanth.medical_service.repository.PrescriptionDetailRepo;
import com.doanth.medical_service.repository.PrescriptionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {
    private final PrescriptionDetailRepo prescriptionDetailRepo;
    private final PrescriptionRepo prescriptionRepo;

    public PrescriptionService(PrescriptionDetailRepo prescriptionDetailRepo, PrescriptionRepo prescriptionRepo) {
        this.prescriptionDetailRepo = prescriptionDetailRepo;
        this.prescriptionRepo = prescriptionRepo;
    }

    public List<PrescriptionDetail> getPrescriptionDetail(Integer PatienId, Integer ExaminationId) {
        List<PrescriptionDetail> prescriptionDetails = prescriptionDetailRepo.findByPatientIdAndExaminationId(PatienId, ExaminationId);

        if(prescriptionDetails == null){
            throw new PresciptionDetailNotFoundException("Prescription details not found");
        }
        return prescriptionDetails;
    }
    public Prescription getPrescription(Integer PatienId, Integer ExaminationId) {
        Prescription prescription= prescriptionRepo.findByPatientIdAndExaminationId(PatienId, ExaminationId);

        if(prescription == null){
            throw new PresciptionDetailNotFoundException("Prescription details not found");
        }

        return prescription;
    }
}
