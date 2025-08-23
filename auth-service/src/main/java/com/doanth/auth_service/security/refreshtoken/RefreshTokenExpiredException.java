package com.doanth.auth_service.security.refreshtoken;

public class RefreshTokenExpiredException extends Exception {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }

}
