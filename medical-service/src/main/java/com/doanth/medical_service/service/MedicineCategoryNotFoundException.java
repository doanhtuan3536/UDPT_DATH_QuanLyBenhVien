package com.doanth.medical_service.service;

public class MedicineCategoryNotFoundException extends RuntimeException{
    public MedicineCategoryNotFoundException(String message) {
        super(message);
    }
}
