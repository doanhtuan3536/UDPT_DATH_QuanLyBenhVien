package com.doanth.appointment_service.serviceclient;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("No user found with given id: " + userId);
    }
}
