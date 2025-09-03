package com.doanth.medical_service.repository;

import com.doanth.medical_service.models.MedicineCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineCategoryRepo extends JpaRepository<MedicineCategory, Integer> {

}
