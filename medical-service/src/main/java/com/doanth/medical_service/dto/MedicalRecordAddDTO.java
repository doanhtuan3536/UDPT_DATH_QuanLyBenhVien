package com.doanth.medical_service.dto;

import java.time.LocalDateTime;

public class MedicalRecordAddDTO {
//    private int medicalRecordId;
    private int doctorId;
    private int patientId;
    private String status;
    private String dischargeSummary;
    private String healthCondition;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "MedicalRecordAddForm{" +
                "doctorId=" + doctorId +
                ", patientId=" + patientId  +
                ", status='" + status + '\'' +
                ", dischargeSummary='" + dischargeSummary + '\'' +
                ", healthCondition='" + healthCondition + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDischargeSummary() {
        return dischargeSummary;
    }

    public void setDischargeSummary(String dischargeSummary) {
        this.dischargeSummary = dischargeSummary;
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
