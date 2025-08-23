package com.doanth.appointment_service.service;

public class SpecialtyWorkDaysException extends RuntimeException {
    public SpecialtyWorkDaysException(String vnDay) {
        super("Specialty does not work on " + vnDay);
    }
}
