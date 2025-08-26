package com.doanth.qlbv_web.serviceClient;

import com.doanth.qlbv_web.dto.AppointmentInfo;
import com.doanth.qlbv_web.dto.ErrorDTO;
import com.doanth.qlbv_web.dto.ListAppointmentInfo;
import com.doanth.qlbv_web.dto.Specialty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AppointmentServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private  AuthServiceClient authServiceClient;
    private final String getSpecialtiesUrl = "http://localhost:8084/api/appointments/specialties";
    private final String AppointmentsUrl = "http://localhost:8084/api/appointments";
    private final String confirmAppointmentUrl = "http://localhost:8084/api/appointments/confirm";
    private final String notconfirmAppointmentUrl = "http://localhost:8084/api/appointments/notconfirm";
    private final String deleteAppointmentUrl = "http://localhost:8084/api/appointments/delete";

//    private final String listAppointmentsForDoctorUrl = "http://localhost:8084/api/appointments/";

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
                    AppointmentsUrl,
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
                        AppointmentsUrl,
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

    public ListAppointmentInfo listAppointments(String accessToken, String status, int page, int size) throws RefreshTokenException, JwtValidationException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> request = new HttpEntity<>(headers);
//        ResponseEntity<PagedModel<AppointmentInfo>> response = null;
        ResponseEntity<String> response = null;

        String newAppointmentsUrl = AppointmentsUrl + "?status=" + status + "&page=" + page + "&size=" + size;

        try {
            response = restTemplate.exchange(
                    newAppointmentsUrl,
                    HttpMethod.GET,
                    request,
                    String.class
            );

            String responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            JsonNode rootNode = objectMapper.readTree(responseBody);

            ListAppointmentInfo result = new ListAppointmentInfo();

            // Extract appointments
            JsonNode embeddedNode = rootNode.get("_embedded");
            if (embeddedNode != null && embeddedNode.has("appointments")) {
                JsonNode appointmentsNode = embeddedNode.get("appointments");
                AppointmentInfo[] appointments = objectMapper.treeToValue(appointmentsNode, AppointmentInfo[].class);
                result.setListAppointments(Arrays.asList(appointments));
                if (result.getListAppointments() != null && result.getListAppointments().size() > 0 && status.equals("resolved")) {
                    {
                        result.setListDoctors(new ArrayList<>());
                        result.getListAppointments().forEach(appointment -> {
                            result.getListDoctors().add(appointment.getDoctorInfoDTO());
                        });
                        System.out.println(result.getListDoctors());
                    }
                }
                else {
                    result.setListDoctors(new ArrayList<>());
                }
            }
            System.out.println(result);
            // Extract page metadata
            JsonNode pageNode = rootNode.get("page");
            if (pageNode != null) {
                PagedModel.PageMetadata pageMetadata = objectMapper.treeToValue(pageNode, PagedModel.PageMetadata.class);
                result.setPageMetadata(pageMetadata);
            }

             // Empty list for doctors

            return result;
//            return appointments.getContent().stream().toList();
        } catch (HttpClientErrorException e) {
            ErrorDTO error = e.getResponseBodyAs(ErrorDTO.class);
            if(error.getErrors().contains("Access token expired")){
                AuthResponse authResponse = authServiceClient.handleAccessTokenExpired();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.add("Authorization", "Bearer " + authResponse.getAccessToken());
//                newHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<?> newRequest = new HttpEntity<>(newHeaders);
                response = restTemplate.exchange(
                        newAppointmentsUrl,
                        HttpMethod.GET,
                        newRequest,
                        String.class
                );
                String responseBody = response.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                JsonNode rootNode = objectMapper.readTree(responseBody);

                ListAppointmentInfo result = new ListAppointmentInfo();

                // Extract appointments
                JsonNode embeddedNode = rootNode.get("_embedded");
                if (embeddedNode != null && embeddedNode.has("appointments")) {
                    JsonNode appointmentsNode = embeddedNode.get("appointments");
                    AppointmentInfo[] appointments = objectMapper.treeToValue(appointmentsNode, AppointmentInfo[].class);
                    result.setListAppointments(Arrays.asList(appointments));
                    if (result.getListAppointments() != null && result.getListAppointments().size() > 0 && status.equals("resolved")) {
                        {
                            result.setListDoctors(new ArrayList<>());
                            result.getListAppointments().forEach(appointment -> {
                                result.getListDoctors().add(appointment.getDoctorInfoDTO());
                            });
                            System.out.println(result.getListDoctors());
                        }
                    }
                    else {
                        result.setListDoctors(new ArrayList<>());
                    }
                }
                System.out.println(result);
                // Extract page metadata
                JsonNode pageNode = rootNode.get("page");
                if (pageNode != null) {
                    PagedModel.PageMetadata pageMetadata = objectMapper.treeToValue(pageNode, PagedModel.PageMetadata.class);
                    result.setPageMetadata(pageMetadata);
                }

//                result.setListDoctors(new ArrayList<>());

                return result;

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



    public AppointmentInfo confirmAppointment(String accessToken, int appointmentId) throws RefreshTokenException, JwtValidationException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<AppointmentInfo> response = null;

        String newAppointmentsUrl = confirmAppointmentUrl + "/" + appointmentId;

        try {
            response = restTemplate.exchange(
                    newAppointmentsUrl,
                    HttpMethod.PUT,
                    request,
                    AppointmentInfo.class
            );

            // Empty list for doctors
            System.out.println(response);


            return response.getBody();
        } catch (HttpClientErrorException e) {
            ErrorDTO error = e.getResponseBodyAs(ErrorDTO.class);
            if(error.getErrors().contains("Access token expired")){
                AuthResponse authResponse = authServiceClient.handleAccessTokenExpired();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.add("Authorization", "Bearer " + authResponse.getAccessToken());
                HttpEntity<AppointmentInfo> newRequest = new HttpEntity<>(newHeaders);
                response = restTemplate.exchange(
                        newAppointmentsUrl,
                        HttpMethod.PUT,
                        newRequest,
                        AppointmentInfo.class
                );
                return response.getBody();

            }

            error.getErrors().forEach((errorMsg)-> {
                if (errorMsg.contains("No appointment found with the given id")){
                    throw new AppointmentNotFoundException(appointmentId);
                }
            });
//
            throw new JwtValidationException(e.getResponseBodyAsString());
        }
    }

    public AppointmentInfo notconfirmAppointment(String accessToken, int appointmentId) throws RefreshTokenException, JwtValidationException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<AppointmentInfo> response = null;

        String newAppointmentsUrl = notconfirmAppointmentUrl + "/" + appointmentId;

        try {
            response = restTemplate.exchange(
                    newAppointmentsUrl,
                    HttpMethod.PUT,
                    request,
                    AppointmentInfo.class
            );

            // Empty list for doctors
            System.out.println(response);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            ErrorDTO error = e.getResponseBodyAs(ErrorDTO.class);
            if(error.getErrors().contains("Access token expired")){
                AuthResponse authResponse = authServiceClient.handleAccessTokenExpired();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.add("Authorization", "Bearer " + authResponse.getAccessToken());
                HttpEntity<AppointmentInfo> newRequest = new HttpEntity<>(newHeaders);
                response = restTemplate.exchange(
                        newAppointmentsUrl,
                        HttpMethod.PUT,
                        newRequest,
                        AppointmentInfo.class
                );
                return response.getBody();

            }

            error.getErrors().forEach((errorMsg)-> {
                if (errorMsg.contains("No appointment found with the given id")){
                    throw new AppointmentNotFoundException(appointmentId);
                }
            });
//
            throw new JwtValidationException(e.getResponseBodyAsString());
        }
    }
    public AppointmentInfo deleteAppointment(String accessToken, int appointmentId) throws RefreshTokenException, JwtValidationException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<AppointmentInfo> response = null;

        String newAppointmentsUrl = deleteAppointmentUrl + "/" + appointmentId;

        try {
            response = restTemplate.exchange(
                    newAppointmentsUrl,
                    HttpMethod.DELETE,
                    request,
                    AppointmentInfo.class
            );

            // Empty list for doctors
            System.out.println(response);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            ErrorDTO error = e.getResponseBodyAs(ErrorDTO.class);
            if(error.getErrors().contains("Access token expired")){
                AuthResponse authResponse = authServiceClient.handleAccessTokenExpired();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.add("Authorization", "Bearer " + authResponse.getAccessToken());
                HttpEntity<?> newRequest = new HttpEntity<>(newHeaders);
                response = restTemplate.exchange(
                        newAppointmentsUrl,
                        HttpMethod.DELETE,
                        newRequest,
                        AppointmentInfo.class
                );
                return response.getBody();

            }

            error.getErrors().forEach((errorMsg)-> {
                if (errorMsg.contains("No appointment found with the given id")){
                    throw new AppointmentNotFoundException(appointmentId);
                }
            });
//
            throw new JwtValidationException(e.getResponseBodyAsString());
        }
    }


}
