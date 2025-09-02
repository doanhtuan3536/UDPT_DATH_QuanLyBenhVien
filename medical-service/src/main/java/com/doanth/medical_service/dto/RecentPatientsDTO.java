package com.doanth.medical_service.dto;

public class RecentPatientsDTO {
    private int userId;
    private int medicalRecordId;
    private int examinationId;

    public RecentPatientsDTO() {}

    public RecentPatientsDTO(int userId, int medicalRecordId, int examinationId) {
        this.userId = userId;
        this.medicalRecordId = medicalRecordId;
        this.examinationId = examinationId;
    }

    public RecentPatientsDTO(int userId, int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(int examinationId) {
        this.examinationId = examinationId;
    }

    @Override
    public String toString() {
        return "RecentPatientsDTO{" +
                "userId=" + userId +
                ", medicalRecordId=" + medicalRecordId +
                ", examinationId=" + examinationId +
                '}';
    }
}
