package com.doanth.medical_service.models;

import jakarta.persistence.*;

@Entity
@Table(name = "DANHMUCTHUOC")
public class MedicineCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "danhmucthuoc_id")
    private int medicineCategoryId;

    @Column(name = "tendanhmuc", nullable = false)
    private String medicineCategoryName;

    @Column(name = "mota")
    @Lob
    private String description;

    public int getMedicineCategoryId() {
        return medicineCategoryId;
    }

    public void setMedicineCategoryId(int medicineCategoryId) {
        this.medicineCategoryId = medicineCategoryId;
    }

    public String getMedicineCategoryName() {
        return medicineCategoryName;
    }

    public void setMedicineCategoryName(String medicineCategoryName) {
        this.medicineCategoryName = medicineCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MedicineCategory{" +
                "medicineCategoryId=" + medicineCategoryId +
                ", medicineCategoryName='" + medicineCategoryName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
