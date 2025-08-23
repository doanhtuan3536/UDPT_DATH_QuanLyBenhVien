package com.doanth.appointment_service.models;

import jakarta.persistence.*;

@Entity
@Table(name = "BACSI")
public class Doctor {
    @Id
    @Column(name = "bacsi_id")
    private Integer doctorId;


    @ManyToOne
    @JoinColumn(name = "chuyenkhoa_id")
    private Specialty specialty;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", specialty=" + specialty +
                '}';
    }
}
