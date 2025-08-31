package com.doanth.medical_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class UserInfoDTO {
    @JsonProperty("userId")
    private Integer doctorId;

    private String email;

    private String hoten;

    private LocalDate ngaysinh;

    private String gioitinh;

    private String sdt;
    private Integer appointmentId;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

//    public Integer getAppointmentId() {
//        return appointmentId;
//    }
//
//    public void setAppointmentId(Integer appointmentId) {
//        this.appointmentId = appointmentId;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public LocalDate getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(LocalDate ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Override
    public String toString() {
        return "DoctorDTO{" +
                "doctorId=" + doctorId +
                ", email='" + email + '\'' +
                ", hoten='" + hoten + '\'' +
                ", ngaysinh=" + ngaysinh +
                ", gioitinh='" + gioitinh + '\'' +
                ", sdt='" + sdt + '\'' +
                ", appointmentId=" + appointmentId +
                '}';
    }
}
