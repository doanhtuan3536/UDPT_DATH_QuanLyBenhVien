package com.doanth.qlbv_web.serviceClient;

import com.doanth.qlbv_web.dto.LoginForm;
import com.doanth.qlbv_web.dto.SignupForm;
import com.doanth.qlbv_web.dto.SignupResult;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String signupUrl = "http://localhost:8081/api/auth/signup";
    private final String loginUrl = "http://localhost:8081/api/auth/login";

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

}
