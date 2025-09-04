package com.doanth.qlbv_web.dto;

public class MedicineInPrescriptionDTO {
    private int medicineId;
    private double quantity;

    public int getMedicineId() {
        return medicineId;
    }
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "MedicineInPrescriptionDTO{" +
                "medicineId=" + medicineId +
                ", quantity=" + quantity +
                '}';
    }
}
