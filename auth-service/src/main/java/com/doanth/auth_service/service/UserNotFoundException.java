package com.doanth.auth_service.service;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("No user found with given id: " + userId);
    }
}
