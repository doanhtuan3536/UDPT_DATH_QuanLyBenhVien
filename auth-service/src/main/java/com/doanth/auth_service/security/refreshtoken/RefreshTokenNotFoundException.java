package com.doanth.auth_service.security.refreshtoken;

public class RefreshTokenNotFoundException extends Exception {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

}
