package com.doanth.medical_service.security;

public class JwtValidationException extends Exception {

    public JwtValidationException(String message) {
        super(message);
    }
}
