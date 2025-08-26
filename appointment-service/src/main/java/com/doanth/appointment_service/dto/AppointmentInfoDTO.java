package com.doanth.appointment_service.dto;

import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalTime;
@Relation(collectionRelation = "appointments")
public class AppointmentInfoDTO{
    private Integer appointmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String note;
    private Integer specialtyId;
    private Integer userId;
    private String status;
    private String specialtyName;
    private String specialtyRoom;
    private DoctorInfoDTO doctorInfoDTO;

    public DoctorInfoDTO getDoctorInfoDTO() {
        return doctorInfoDTO;
    }

    public void setDoctorInfoDTO(DoctorInfoDTO doctorInfoDTO) {
        this.doctorInfoDTO = doctorInfoDTO;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public String getSpecialtyRoom() {
        return specialtyRoom;
    }

    public void setSpecialtyRoom(String specialtyRoom) {
        this.specialtyRoom = specialtyRoom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AppointmentInfoDTO{" +
                "appointmentId=" + appointmentId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", note='" + note + '\'' +
                ", specialtyId=" + specialtyId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", specialtyName='" + specialtyName + '\'' +
                ", specialtyRoom='" + specialtyRoom + '\'' +
                ", doctorInfoDTO=" + doctorInfoDTO +
                '}';
    }
}
