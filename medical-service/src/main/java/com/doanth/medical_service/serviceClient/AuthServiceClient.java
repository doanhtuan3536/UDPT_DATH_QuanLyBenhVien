package com.doanth.medical_service.serviceClient;

import com.doanth.medical_service.dto.DoctorInfoDTO;
import com.doanth.medical_service.dto.LoginForm;
import com.doanth.medical_service.security.ErrorDTO;
import com.doanth.medical_service.security.JwtValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AuthServiceClient {
    @Value("${service.auth.username}")
    private String serviceUsername;

    @Value("${service.auth.password}")
    private String secretPassword;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String validateAccessTokenUrl = "http://localhost:8081/api/auth/token/validate";
    private final String serviceLoginUrl = "http://localhost:8081/api/auth/service/login";
    private final String userInfoUrl = "http://localhost:8081/api/auth/user";
    public Claims validateAccessToken(String AccessTokenJwt) throws JwtValidationException {// URL của Auth Service

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(AccessTokenJwt, headers);
        System.out.println(request);
        ResponseEntity<Map<String, Object>> response = null;

        try{
            response= restTemplate.exchange(
                    validateAccessTokenUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            System.out.println(response.getBody());
//            ObjectMapper mapper = new ObjectMapper();

//            return mapper.readValue(response.getBody(), Claims.class);
            return new DefaultClaims(response.getBody());
        }
        catch(HttpClientErrorException e){
//            e.printStackTrace();
            String msg = e.getResponseBodyAsString();
            System.out.println("AuthServiceClient validateAccessToken");
            System.out.println(msg);
//            if (msg.equals("Access token expired")) throw new JwtValidationException(msg, new ExpiredJwtException());
            throw new JwtValidationException(e.getResponseBodyAs(String.class));
        }
    }
    public String login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginForm form = new LoginForm();
        form.setUsername(username);
        form.setPassword(password);

        HttpEntity<LoginForm> request = new HttpEntity<>(form, headers);
//        System.out.println(request);
        ResponseEntity<String> response = null;

        try{
            response= restTemplate.exchange(
                    serviceLoginUrl, HttpMethod.POST, request, String.class
            );
            System.out.println(response);
            return response.getBody();
        }
        catch(HttpClientErrorException e){
            throw new AccessTokenForServiceException("authentication for access token failed or the auth service server is down");
        }
    }

    public DoctorInfoDTO getDoctorInfo(Integer doctorId, String accessToken) throws JwtValidationException {
//        Claims claims = validateAccessToken(AccessTokenJwt);
//        return (DoctorInfoDTO) claims.get("doctorInfoDTO");
//        String accessToken = login(serviceUsername, secretPassword);
        String url = userInfoUrl + "/" + doctorId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<DoctorInfoDTO> response = null;
        try{
            response= restTemplate.exchange(
                    url, HttpMethod.GET, request, DoctorInfoDTO.class
            );
            return response.getBody();
        }
        catch(HttpClientErrorException e){
            ErrorDTO error = e.getResponseBodyAs(ErrorDTO.class);
            error.getErrors().forEach((errorMsg)-> {
                if (errorMsg.contains("No user found with given id")){
                    throw new UserNotFoundException((long)doctorId);
                }
            });
            throw new JwtValidationException(e.getResponseBodyAs(String.class));
        }
    }


}
