package com.doanth.appointment_service.service;

public class SpecialtyNotFoundException extends RuntimeException{
    public SpecialtyNotFoundException(Integer specialtyId) {
        super("No specialty found with the given id: " + specialtyId);
    }

}
