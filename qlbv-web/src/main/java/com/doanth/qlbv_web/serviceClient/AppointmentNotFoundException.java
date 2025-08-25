package com.doanth.qlbv_web.serviceClient;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(Integer id) {
        super("No appointment found with the given id: " + id);
    }
}
