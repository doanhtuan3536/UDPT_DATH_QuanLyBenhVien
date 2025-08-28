package com.doanth.qlbv_web.controllers;

import com.doanth.qlbv_web.dto.MedicalRecordFullDTO;
import com.doanth.qlbv_web.dto.MedicalRecordShortDTO;
import com.doanth.qlbv_web.dto.UserDetails;
import com.doanth.qlbv_web.serviceClient.AuthResponse;
import com.doanth.qlbv_web.serviceClient.JwtValidationException;
import com.doanth.qlbv_web.serviceClient.MedicalServiceClient;
import com.doanth.qlbv_web.serviceClient.RefreshTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/medical-records")
public class MedicalController {
    private final MedicalServiceClient medicalServiceClient;

    public MedicalController(MedicalServiceClient medicalServiceClient) {
        this.medicalServiceClient = medicalServiceClient;
    }
    @GetMapping
    public String medicalRecords(Model model) throws RefreshTokenException, JwtValidationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();
        String accessToken = loggedUser.getAccessToken();
        List<MedicalRecordShortDTO> medicalRecordShortDTOSDTO = medicalServiceClient.getRecordsByPatientId(accessToken);
//        System.out.println(medicalRecordShortDTOSDTO);
        model.addAttribute("medicalRecords", medicalRecordShortDTOSDTO);
        return "medical_records";
    }
    @GetMapping("/details/{id}")
    public String medicalRecordsDetails(@PathVariable("id") Integer id, Model model) throws RefreshTokenException, JwtValidationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();
        String accessToken = loggedUser.getAccessToken();
        MedicalRecordFullDTO medicalRecordFullDTO = medicalServiceClient.getRecordDetailsByRecordId(accessToken, id);
//        System.out.println(medicalRecordFullDTO);
        model.addAttribute("record", medicalRecordFullDTO);
        return "medical_record_details";
    }

    @GetMapping("/prescriptions/{id}")
    public String prescriptions(@PathVariable("id") Integer id, Model model) throws RefreshTokenException, JwtValidationException {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();
//        String accessToken = loggedUser.getAccessToken();
//        MedicalRecordFullDTO medicalRecordFullDTO = medicalServiceClient.getRecordDetailsByRecordId(accessToken, id);
        return "prescription_details";
    }
}
