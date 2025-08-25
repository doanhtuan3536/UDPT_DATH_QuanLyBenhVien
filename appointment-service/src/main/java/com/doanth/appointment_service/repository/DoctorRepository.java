package com.doanth.appointment_service.repository;

import com.doanth.appointment_service.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("SELECT l.specialty.specialtyId FROM Doctor l WHERE l.doctorId = ?1")
    public Integer findSpecialtyIdByDoctorId(Integer doctorId);
}
