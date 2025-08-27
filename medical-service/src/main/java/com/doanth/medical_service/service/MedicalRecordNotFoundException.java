package com.doanth.medical_service.service;

public class MedicalRecordNotFoundException extends RuntimeException{
    public MedicalRecordNotFoundException(String message) {
        super(message);
    }
}
