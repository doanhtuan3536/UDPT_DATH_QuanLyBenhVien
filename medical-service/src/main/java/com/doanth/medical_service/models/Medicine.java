package com.doanth.medical_service.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "THUOC")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thuoc_id")
    private int medicineId;

    @Column(name = "tenthuoc", nullable = false)
    private String medicineName;

    @Column(name = "soluong", nullable = false)
    private Integer stockQuantity; // Số lượng thuốc còn trong kho

    @Column(name = "giaban", nullable = false)
    private Double price;

    @Column(name = "mota")
    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "danhmucthuoc_id")
    private MedicineCategory medicineCategory;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionDetail> prescriptionDetails;

    // Constructors
    public Medicine() {}

    public Medicine(String medicineName, Integer stockQuantity, Double price, String description) {
        this.medicineName = medicineName;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
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

    public List<PrescriptionDetail> getPrescriptionDetails() {
        return prescriptionDetails;
    }

    public void setPrescriptionDetails(List<PrescriptionDetail> prescriptionDetails) {
        this.prescriptionDetails = prescriptionDetails;
    }
}
