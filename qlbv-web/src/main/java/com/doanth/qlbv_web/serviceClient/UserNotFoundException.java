package com.doanth.qlbv_web.serviceClient;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("No user found with given id: " + userId);
    }
}
