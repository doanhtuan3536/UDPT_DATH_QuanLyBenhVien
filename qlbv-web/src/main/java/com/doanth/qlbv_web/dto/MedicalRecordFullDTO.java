package com.doanth.qlbv_web.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalRecordFullDTO {
    private int medicalRecordId;
    private int userId;
    private int doctorId;
    private String doctorName;
    private String status;
    private String dischargeSummary;
    private String healthCondition;
    private LocalDateTime createdAt;
    List<ExaminationDTO> examinations;

    @Override
    public String toString() {
        return "MedicalRecordFullDTO{" +
                "medicalRecordId=" + medicalRecordId +
                ", userId=" + userId +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", status='" + status + '\'' +
                ", dischargeSummary='" + dischargeSummary + '\'' +
                ", healthCondition='" + healthCondition + '\'' +
                ", createdAt=" + createdAt +
                ", examinations=" + examinations +
                '}';
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public List<ExaminationDTO> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<ExaminationDTO> examinations) {
        this.examinations = examinations;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    //    private List<Examination> examinations;
}
