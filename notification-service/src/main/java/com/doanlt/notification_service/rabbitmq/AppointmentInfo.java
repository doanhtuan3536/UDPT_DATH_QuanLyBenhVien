package com.doanlt.notification_service.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentInfo {
    @JsonProperty("appointmentId")
    private Integer appointmentId;

    @JsonProperty("appointmentDate")
    private LocalDate appointmentDate;

    @JsonProperty("appointmentTime")
    private LocalTime appointmentTime;

    @JsonProperty("note")
    private String note;

    @JsonProperty("specialtyId")
    private Integer specialtyId;

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("specialtyName")
    private String specialtyName;

    @JsonProperty("specialtyRoom")
    private String specialtyRoom;

    @JsonProperty("doctorInfoDTO")
    private DoctorDTO doctorInfoDTO;

    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public DoctorDTO getDoctorInfoDTO() {
        return doctorInfoDTO;
    }

    public void setDoctorInfoDTO(DoctorDTO doctorInfoDTO) {
        this.doctorInfoDTO = doctorInfoDTO;
    }

    public AppointmentInfo() {}

    public AppointmentInfo(Integer appointmentId, LocalDate appointmentDate, LocalTime appointmentTime, String note, Integer specialtyId,
                           Integer userId, String status, String specialtyName, String specialtyRoom, DoctorDTO doctorInfoDTO) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.note = note;
        this.specialtyId = specialtyId;
        this.userId = userId;
        this.status = status;
        this.specialtyName = specialtyName;
        this.specialtyRoom = specialtyRoom;
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
        return "AppointmentInfo{" +
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
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
