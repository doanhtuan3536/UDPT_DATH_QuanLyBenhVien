package com.doanth.medical_service.dto;

import java.time.LocalDateTime;

public class MedicalRecordShortDTO {
    private int medicalRecordId;
    private int doctorId;
    private String doctorName;
    private String status;
    private String healthCondition;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "MedicalRecordShortDTO{" +
                "medicalRecordId=" + medicalRecordId +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", status='" + status + '\'' +
                ", healthCondition='" + healthCondition + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
