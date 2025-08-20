package com.doanth.auth_service.security.jwt;

public class JwtValidationException extends Exception {

    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
