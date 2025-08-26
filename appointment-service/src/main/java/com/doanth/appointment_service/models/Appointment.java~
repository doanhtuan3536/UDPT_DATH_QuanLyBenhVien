package com.doanth.appointment_service.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "LICHKHAMBENH")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lichkhambenh_id")
    private Integer appointmentId;

    @Column(name = "ngaykhambenh", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "giokhambenh", nullable = false)
    private LocalTime appointmentTime;

    @Column(name = "trangthai", nullable = false, length = 20)
    private String status;

    @Column(name = "ghichu")
    @Lob
    private String note;

    @Column(name = "benhnhan_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuyenkhoa_id")
    private Specialty specialty;

    @ManyToOne
    @JoinColumn(name = "bacsi_id")
    private Doctor doctor;



    private boolean trashed;

    public boolean isTrashed() {
        return trashed;
    }

    public void setTrashed(boolean trashed) {
        this.trashed = trashed;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", status='" + status + '\'' +
                ", note='" + note + '\'' +
                ", userId=" + userId +
                ", specialty=" + specialty +
                ", doctor=" + doctor +
                ", trashed=" + trashed +
                '}';
    }
}
