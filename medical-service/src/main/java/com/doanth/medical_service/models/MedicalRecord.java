package com.doanth.medical_service.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "HOSOBENHAN")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hosobenhan_id")
    private int medicalRecordId;
    @Column(name = "benhnhan_id", nullable = false)
    private int userId;
    @Column(name = "bacsi_id", nullable = false)
    private int doctorId;
    @Column(name = "trangthai", nullable = false)
    private String status;
    @Column(name = "tomtatquatrinhdieutri")
    @Lob
    private String dischargeSummary;
    @Column(name = "tinhtrangsuckhoe")
    @Lob
    private String healthCondition;
    @Column(name = "ngaytao", nullable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "medicalRecord")
    private List<Examination> examinations;

    public List<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<Examination> examinations) {
        this.examinations = examinations;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
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

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "medicalRecordId=" + medicalRecordId +
                ", userId=" + userId +
                ", doctorId=" + doctorId +
                ", status='" + status + '\'' +
                ", dischargeSummary='" + dischargeSummary + '\'' +
                ", healthCondition='" + healthCondition + '\'' +
                ", createdAt=" + createdAt +
                ", examinations=" + examinations +
                '}';
    }
}
