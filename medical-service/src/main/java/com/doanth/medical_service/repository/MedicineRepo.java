package com.doanth.medical_service.repository;

import com.doanth.medical_service.models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepo extends JpaRepository<Medicine, Integer> {
    @Query("SELECT m FROM Medicine m WHERE m.medicineCategory.medicineCategoryId = ?1")
    List<Medicine> findByCategoryId(Integer categoryId);

    @Query("SELECT m.price FROM Medicine m WHERE m.medicineId = ?1")
    Double getPriceByMedicineId(Integer medicineId);
}
