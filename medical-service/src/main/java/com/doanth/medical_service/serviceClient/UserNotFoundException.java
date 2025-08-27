package com.doanth.medical_service.serviceClient;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("No user found with given id: " + userId);
    }
}
