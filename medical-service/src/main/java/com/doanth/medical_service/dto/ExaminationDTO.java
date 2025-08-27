package com.doanth.medical_service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExaminationDTO {
    private int examinationId;

    private int doctorId;
    private String doctorName;

    private LocalDate date;

    private LocalTime time;

    private String subjective_note;

    private String objective_note;

    private String assessment_note;

    @Override
    public String toString() {
        return "ExaminationDTO{" +
                "examinationId=" + examinationId +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
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
