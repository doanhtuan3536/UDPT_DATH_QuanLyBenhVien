package com.doanth.qlbv_web.serviceClient;

import com.doanth.qlbv_web.dto.AppointmentInfo;
import com.doanth.qlbv_web.dto.ErrorDTO;
import com.doanth.qlbv_web.dto.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class AppointmentServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private  AuthServiceClient authServiceClient;
    private final String getSpecialtiesUrl = "http://localhost:8084/api/appointments/specialties";
    private final String makeAppointmentUrl = "http://localhost:8084/api/appointments";

    public List<Specialty> getSpecialties(String accessToken) throws RefreshTokenException, JwtValidationException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<List<Specialty>> response = null;

        try {
            response = restTemplate.exchange(
                    getSpecialtiesUrl,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<Specialty>>() {}
            );
            System.out.println(response);
            return response.getBody();
        } catch (HttpClientErrorException e) {
//            System.out.println("catch AppointmentServiceClient getSpecialties HttpClientErrorException");
            ErrorDTO error = e.getResponseBodyAs(ErrorDTO.class);
            if(error.getErrors().contains("Access token expired")){
//                System.out.println("catch AppointmentServiceClient getSpecialties if Access token expired");
                AuthResponse authResponse = authServiceClient.handleAccessTokenExpired();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.add("Authorization", "Bearer " + authResponse.getAccessToken());
                HttpEntity<?> newRequest = new HttpEntity<>(newHeaders);
                response = restTemplate.exchange(
                        getSpecialtiesUrl,
                        HttpMethod.GET,
                        newRequest,
                        new ParameterizedTypeReference<List<Specialty>>() {}
                );
                return response.getBody();

            };
            throw new JwtValidationException(e.getResponseBodyAsString());
        }
    }

    public AppointmentInfo makeAppointment(AppointmentInfo form, String accessToken) throws RefreshTokenException, JwtValidationException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AppointmentInfo> request = new HttpEntity<>(form,headers);
        ResponseEntity<AppointmentInfo> response = null;

        try {
            response = restTemplate.exchange(
                    makeAppointmentUrl,
                    HttpMethod.POST,
                    request,
                    AppointmentInfo.class
            );
            System.out.println(response);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            ErrorDTO error = e.getResponseBodyAs(ErrorDTO.class);
            if(error.getErrors().contains("Access token expired")){
                AuthResponse authResponse = authServiceClient.handleAccessTokenExpired();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.add("Authorization", "Bearer " + authResponse.getAccessToken());
                newHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<AppointmentInfo> newRequest = new HttpEntity<>(form,newHeaders);
                response = restTemplate.exchange(
                        makeAppointmentUrl,
                        HttpMethod.POST,
                        newRequest,
                        AppointmentInfo.class
                );
                return response.getBody();

            }
            error.getErrors().forEach((errorMsg)-> {
                if (errorMsg.contains("Specialty does not work on")){
                    throw new SpecialtyWorkDaysException();
                }
                else if (errorMsg.contains("No specialty found with the given id")){
                    throw new SpecialtyNotFoundException();
                }
                else if (errorMsg.contains("Appointment time not within working hours")){
                    throw new AppointmentTimeNotBetweenWorkingHoursException();
                }
            });

            throw new JwtValidationException(e.getResponseBodyAsString());
        }
    }


}
