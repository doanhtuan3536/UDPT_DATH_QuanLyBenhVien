package com.doanth.qlbv_web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medical-records")
public class MedicalController {
    @GetMapping
    public String medicalRecords() {
        return "medical_records";
    }
    @GetMapping("/details/{id}")
    public String medicalRecordsDetails(@PathVariable("id") Integer id) {
        return "medical_record_details";
    }
}
