package com.doanth.medical_service.repository;

import com.doanth.medical_service.models.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationRepo extends JpaRepository<Examination, Integer> {
}
