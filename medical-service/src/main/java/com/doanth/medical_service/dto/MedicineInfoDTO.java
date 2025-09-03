package com.doanth.medical_service.dto;

public class MedicineInfoDTO {
    private int medicineId;
    private String medicineName;
    private Integer stockQuantity;
    private Double price;
    private String description;
    private int medicineCategoryId;
    private String medicineCategoryName;

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    @Override
    public String toString() {
        return "MedicineInfoDTO{" +
                "medicineId=" + medicineId +
                ", medicineName='" + medicineName + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", medicineCategoryId=" + medicineCategoryId +
                ", medicineCategoryName='" + medicineCategoryName + '\'' +
                '}';
    }
}
