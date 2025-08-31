package com.doanth.medical_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PrecriptionDetailId {
    private PrescriptionId prescriptionId;

    @Column(name = "thuoc_id")
    private int medicineId;

    public PrecriptionDetailId() {}

    public PrecriptionDetailId(PrescriptionId prescriptionId, int medicineId) {
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
    }

    public PrescriptionId getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(PrescriptionId prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    @Override
    public String toString() {
        return "PrecriptionDetailId{" +
                "prescriptionId=" + prescriptionId +
                ", medicineId=" + medicineId +
                '}';
    }
}
