package com.doanth.appointment_service.service;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(Integer id) {
        super("No appointment found with the given id: " + id);
    }
}
