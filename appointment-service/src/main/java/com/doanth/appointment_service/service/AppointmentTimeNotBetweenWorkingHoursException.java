package com.doanth.appointment_service.service;

public class AppointmentTimeNotBetweenWorkingHoursException extends RuntimeException{
    public AppointmentTimeNotBetweenWorkingHoursException(String message) {
        super(message);
    }
}
