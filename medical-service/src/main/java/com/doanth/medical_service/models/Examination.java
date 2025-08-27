package com.doanth.medical_service.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "LICHSUKHAM")
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lichsukham_id")
    private int examinationId;

    @ManyToOne
    @JoinColumn(name = "hosobenhan_id")
    private MedicalRecord medicalRecord;

    @Column(name = "bacsi_id", nullable = false)
    private int doctorId;

    @Column(name = "ngaykham", nullable = false)
    private LocalDate date;

    @Column(name = "giokham", nullable = false)
    private LocalTime time;

    @Column(name = "trieuchungchuquan")
    @Lob
    private String subjective_note;

    @Column(name = "trieuchungkhachquan")
    @Lob
    private String objective_note;

    @Column(name = "chandoan")
    @Lob
    private String assessment_note;

    @Override
    public String toString() {
        return "Examination{" +
                "examinationId=" + examinationId +
                ", medicalRecord=" + medicalRecord +
                ", doctorId=" + doctorId +
                ", date=" + date +
                ", time=" + time +
                ", subjective_note='" + subjective_note + '\'' +
                ", objective_note='" + objective_note + '\'' +
                ", assessment_note='" + assessment_note + '\'' +
                '}';
    }

    public int getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(int examinationId) {
        this.examinationId = examinationId;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getSubjective_note() {
        return subjective_note;
    }

    public void setSubjective_note(String subjective_note) {
        this.subjective_note = subjective_note;
    }

    public String getObjective_note() {
        return objective_note;
    }

    public void setObjective_note(String objective_note) {
        this.objective_note = objective_note;
    }

    public String getAssessment_note() {
        return assessment_note;
    }

    public void setAssessment_note(String assessment_note) {
        this.assessment_note = assessment_note;
    }
}
