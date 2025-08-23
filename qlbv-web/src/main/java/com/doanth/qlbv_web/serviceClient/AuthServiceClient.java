package com.doanth.qlbv_web.serviceClient;

import com.doanth.qlbv_web.dto.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String signupUrl = "http://localhost:8081/api/auth/signup";
    private final String loginUrl = "http://localhost:8081/api/auth/login";
    private final String refreshTokenUrl = "http://localhost:8081/api/auth/token/refresh";
    private final String newTokenUrl = "http://localhost:8081/api/auth/token";

    public SignupResult signup(SignupForm form) {// URL của Auth Service

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SignupForm> request = new HttpEntity<>(form, headers);
        System.out.println(request);
        ResponseEntity<SignupResult> response = null;

        try{
            response= restTemplate.exchange(
                    signupUrl, HttpMethod.POST, request, SignupResult.class
            );
            return response.getBody();
        }
        catch(HttpClientErrorException e){
            return e.getResponseBodyAs(SignupResult.class);
        }
    }

    public AuthResponse login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginForm form = new LoginForm();
        form.setUsername(username);
        form.setPassword(password);

        HttpEntity<LoginForm> request = new HttpEntity<>(form, headers);
//        System.out.println(request);
        ResponseEntity<AuthResponse> response = null;

        try{
            response= restTemplate.exchange(
                    loginUrl, HttpMethod.POST, request, AuthResponse.class
            );
            System.out.println(response);
            return response.getBody();
        }
        catch(HttpClientErrorException e){
            throw new RuntimeException("Xác thực thất bại");
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest requestTokenRefresh) throws RefreshTokenException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RefreshTokenRequest> request = new HttpEntity<>(requestTokenRefresh, headers);
//        System.out.println(request);
        ResponseEntity<AuthResponse> response = null;

        try{
            response= restTemplate.exchange(
                    refreshTokenUrl, HttpMethod.POST, request, AuthResponse.class
            );
            System.out.println(response);
            return response.getBody();
        }
        catch(HttpClientErrorException e){
            String msg = e.getResponseBodyAsString();
            System.out.println(msg);
            throw new RefreshTokenException(msg);
        }
    }

    public AuthResponse getNewToken(String username, String password){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginForm form = new LoginForm();
        form.setUsername(username);
        form.setPassword(password);

        HttpEntity<LoginForm> request = new HttpEntity<>(form, headers);
//        System.out.println(request);
        ResponseEntity<AuthResponse> response = null;

        try{
            response= restTemplate.exchange(
                    newTokenUrl, HttpMethod.POST, request, AuthResponse.class
            );
            System.out.println(response);
            return response.getBody();
        }
        catch(HttpClientErrorException e){
            throw new RuntimeException("Lấy token thất bại");
        }
    }

    public AuthResponse handleAccessTokenExpired() throws RefreshTokenException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String refreshToken = userDetails.getAuthResponse().getRefreshToken();
        RefreshTokenRequest requestTokenRefresh = new RefreshTokenRequest();
        requestTokenRefresh.setUsername(userDetails.getUserName());
        requestTokenRefresh.setRefreshToken(refreshToken);
        AuthResponse authResponse = this.refreshToken(requestTokenRefresh);
        System.out.println(authResponse);
        userDetails.setAuthResponse(authResponse);
       return authResponse;
    }

}
