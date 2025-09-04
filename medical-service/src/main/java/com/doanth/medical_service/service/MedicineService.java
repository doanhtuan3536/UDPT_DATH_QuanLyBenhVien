package com.doanth.medical_service.service;

import com.doanth.medical_service.dto.MedicineInfoDTO;
import com.doanth.medical_service.models.Medicine;
import com.doanth.medical_service.models.MedicineCategory;
import com.doanth.medical_service.repository.MedicineCategoryRepo;
import com.doanth.medical_service.repository.MedicineRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService {
    private final MedicineCategoryRepo medicineCategoryRepo;
    private final MedicineRepo medicineRepo;
    private final ModelMapper modelMapper;

    public MedicineService(MedicineCategoryRepo medicineCategoryRepo, MedicineRepo medicineRepo, ModelMapper modelMapper) {
        this.medicineCategoryRepo = medicineCategoryRepo;
        this.medicineRepo = medicineRepo;
        this.modelMapper = modelMapper;
    }

    public List<MedicineCategory> getAllMedicineCategories() {
        return medicineCategoryRepo.findAll();
    }

    public List<MedicineInfoDTO> getMedicinesByCategoryId(Integer categoryId) {
        MedicineCategory medicineCategory = medicineCategoryRepo.findById(categoryId).orElse(null);
        if (medicineCategory == null) {
            throw new MedicineCategoryNotFoundException("Medicine category not found");
        }
        List<Medicine> medicines = medicineRepo.findByCategoryId(categoryId);
        List<MedicineInfoDTO> medicineInfoDTOS = new java.util.ArrayList<>();
        for (Medicine medicine : medicines) {
            medicineInfoDTOS.add(modelMapper.map(medicine, MedicineInfoDTO.class));
        }
        return medicineInfoDTOS;
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepo.findAll();
    }
    public double getMedicinePrice(int medicineId) {
        return medicineRepo.getPriceByMedicineId(medicineId);
    }
}
