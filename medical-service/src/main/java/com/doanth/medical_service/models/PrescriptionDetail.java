package com.doanth.medical_service.models;

import jakarta.persistence.*;

@Entity
@Table(name = "CHITIETDONTHUOC")
public class PrescriptionDetail {
    @EmbeddedId
    private PrecriptionDetailId precriptionDetailId;

    @ManyToOne
    @MapsId("medicineId")
    @JoinColumn(name = "thuoc_id")
    private Medicine medicine;


    @ManyToOne
    @MapsId("prescriptionId")
    @JoinColumns({
            @JoinColumn(name = "benhnhan_id", referencedColumnName = "benhnhan_id"),
            @JoinColumn(name = "lichsukham_id", referencedColumnName = "lichsukham_id")
    })
    private Prescription prescription;


    @Column(name = "soluong", nullable = false)
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PrescriptionDetail() {}

    public PrescriptionDetail(PrecriptionDetailId precriptionDetailId, Medicine medicine, Prescription prescription, int quantity) {
        this.precriptionDetailId = precriptionDetailId;
        this.medicine = medicine;
        this.prescription = prescription;
        this.quantity = quantity;
    }

    public PrecriptionDetailId getPrecriptionDetailId() {
        return precriptionDetailId;
    }

    public void setPrecriptionDetailId(PrecriptionDetailId precriptionDetailId) {
        this.precriptionDetailId = precriptionDetailId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    @Override
    public String toString() {
        return "PrescriptionDetail{" +
                "precriptionDetailId=" + precriptionDetailId +
                ", medicine=" + medicine +
                ", prescription=" + prescription +
                ", quantity=" + quantity +
                '}';
    }
}
