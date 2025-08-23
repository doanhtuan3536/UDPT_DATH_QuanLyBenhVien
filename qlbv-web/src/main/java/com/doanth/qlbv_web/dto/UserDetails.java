package com.doanth.qlbv_web.dto;

import com.doanth.qlbv_web.serviceClient.AuthResponse;

public class UserDetails {
    private String userName;
    private String hoten;
    private AuthResponse authResponse;

    public AuthResponse getAuthResponse() {
        return authResponse;
    }

    public void setAuthResponse(AuthResponse authResponse) {
        this.authResponse = authResponse;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }
}
