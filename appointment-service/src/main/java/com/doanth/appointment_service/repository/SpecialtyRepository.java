package com.doanth.appointment_service.repository;

import com.doanth.appointment_service.dto.SpecialtyInforForListAppointments;
import com.doanth.appointment_service.models.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    @Query("SELECT l.room,l.name FROM Specialty l WHERE l.specialtyId = ?1")
    public SpecialtyInforForListAppointments findNameRoomBySpecialtyId(Integer specialtyId);
}
