package com.doanth.medical_service;

import com.doanth.medical_service.dto.*;
import com.doanth.medical_service.models.*;
import com.doanth.medical_service.security.JwtValidationException;
import com.doanth.medical_service.security.User;
import com.doanth.medical_service.service.ExaminationService;
import com.doanth.medical_service.service.MedicalRecordService;
import com.doanth.medical_service.service.MedicineService;
import com.doanth.medical_service.service.PrescriptionService;
import com.doanth.medical_service.serviceClient.AuthServiceClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/medical")
public class MedicalController {
    private final MedicalRecordService medicalRecordService;
    private ModelMapper modelMapper;
    private AuthServiceClient authServiceClient;
    private PrescriptionService prescriptionService;
    private final ExaminationService examinationService;
    private final MedicineService medicineService;
    @Value("${service.auth.username}")
    private String serviceUsername;

    @Value("${service.auth.password}")
    private String secretPassword;

    public MedicalController(MedicalRecordService medicalRecordService, ModelMapper modelMapper,
                             AuthServiceClient authServiceClient, PrescriptionService prescriptionService,
                             ExaminationService examinationService, MedicineService medicineService) {
        this.medicalRecordService = medicalRecordService;
        this.modelMapper = modelMapper;
        this.authServiceClient = authServiceClient;
        this.prescriptionService = prescriptionService;
        this.examinationService = examinationService;
        this.medicineService = medicineService;
    }

    @GetMapping("/medicines/categories")
    public ResponseEntity<?> GetMedicineCategories() {
//        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        List<MedicalRecord> medicalRecords = medicalRecordService.listByPatientId(user.getUserId());
//        List<MedicalRecordShortDTO> medicalRecordShortDTOS = listEntity2ListDTO(medicalRecords);
//        String accessToken = authServiceClient.login(serviceUsername, secretPassword);
//        int size = medicalRecordShortDTOS.size();
//        for (int i = 0; i < size; i++) {
//            medicalRecordShortDTOS.get(i).setDoctorName(authServiceClient.getUserInfo(medicalRecordShortDTOS.get(i).getDoctorId(), accessToken).getHoten());
//        }
        return ResponseEntity.ok(medicineService.getAllMedicineCategories());
    }

    @GetMapping("/medicines")
    public ResponseEntity<?> GetMedicines(@RequestParam(value = "categoryId", required = false) Integer categoryId) {
        if(categoryId != null) {
            return ResponseEntity.ok(medicineService.getMedicinesByCategoryId(categoryId));
        }

        List<Medicine> medicines = medicineService.getAllMedicines();
        List<MedicineInfoDTO> medicineInfoDTOS = medicines.stream().map(medicine -> modelMapper.map(medicine, MedicineInfoDTO.class)).collect(Collectors.toList());
//        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        List<MedicalRecord> medicalRecords = medicalRecordService.listByPatientId(user.getUserId());
//        List<MedicalRecordShortDTO> medicalRecordShortDTOS = listEntity2ListDTO(medicalRecords);
//        String accessToken = authServiceClient.login(serviceUsername, secretPassword);
//        int size = medicalRecordShortDTOS.size();
//        for (int i = 0; i < size; i++) {
//            medicalRecordShortDTOS.get(i).setDoctorName(authServiceClient.getUserInfo(medicalRecordShortDTOS.get(i).getDoctorId(), accessToken).getHoten());
//        }
        return ResponseEntity.ok(medicineInfoDTOS);
    }

    @GetMapping("/records")
    public ResponseEntity<?> patientGetMedicalRecords() throws JwtValidationException {
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<MedicalRecord> medicalRecords = medicalRecordService.listByPatientId(user.getUserId());
        List<MedicalRecordShortDTO> medicalRecordShortDTOS = listEntity2ListDTO(medicalRecords);
        String accessToken = authServiceClient.login(serviceUsername, secretPassword);
        int size = medicalRecordShortDTOS.size();
        for (int i = 0; i < size; i++) {
            medicalRecordShortDTOS.get(i).setDoctorName(authServiceClient.getUserInfo(medicalRecordShortDTOS.get(i).getDoctorId(), accessToken).getHoten());
        }
        return ResponseEntity.ok(medicalRecordShortDTOS);
    }
    @PostMapping("/records/add")
    public ResponseEntity<?> addMedicalRecord(@RequestBody MedicalRecordAddDTO medicalRecord) throws JwtValidationException {

        MedicalRecord medicalRecordToSave = modelMapper.map(medicalRecord, MedicalRecord.class);

        MedicalRecord savedMedicalRecord = medicalRecordService.add(medicalRecordToSave);

        MedicalRecordAddDTO medicalRecordAddDTO = modelMapper.map(savedMedicalRecord, MedicalRecordAddDTO.class);
        return ResponseEntity.ok(medicalRecordAddDTO);
    }
    @PostMapping("/examinations/add")
    public ResponseEntity<?> addExamination(@RequestBody ExaminationInfoAddDTO examination) throws JwtValidationException {

        Examination examinationToSave = modelMapper.map(examination, Examination.class);
        Examination savedExamination = examinationService.addExamination(examinationToSave);

        Prescription prescription = new Prescription();
        PrescriptionId prescriptionId = new PrescriptionId();
        prescription.setPrescriptionId(prescriptionId);
        prescription.getPrescriptionId().setExaminationId(savedExamination.getExaminationId());
        prescription.getPrescriptionId().setPatientId(examination.getPatientId());
        prescription.setExamination(savedExamination);
        prescription.setStatus("Chưa sẵn sàng");
        prescription.setCreatedAt(LocalDateTime.of(savedExamination.getDate(), savedExamination.getTime()));
        double totalPrice = 0;
        prescription.setPrescriptionDetails(new ArrayList<>());
        for (MedicineInPrescriptionDTO medicine : examination.getMedicineInPrescriptions()) {
            PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
            prescriptionDetail.setPrecriptionDetailId(new PrecriptionDetailId());
            prescriptionDetail.getPrecriptionDetailId().setPrescriptionId(prescription.getPrescriptionId());
            prescriptionDetail.getPrecriptionDetailId().setMedicineId(medicine.getMedicineId());
            double medicinePrice = medicineService.getMedicinePrice(medicine.getMedicineId());
            totalPrice += medicinePrice * medicine.getQuantity();
            prescriptionDetail.setQuantity((int) medicine.getQuantity());
            Medicine medicine1 = new Medicine();
            medicine1.setMedicineId(medicine.getMedicineId());
            prescriptionDetail.setMedicine(medicine1);
            prescriptionDetail.setPrescription(prescription);
            prescription.getPrescriptionDetails().add(prescriptionDetail);
        }
        prescription.setTotalPrice(totalPrice);
        prescriptionService.addPrescription(prescription);

        return ResponseEntity.ok(savedExamination.getExaminationId());
    }
    @GetMapping("/doctor/records/{patientId}")
    public ResponseEntity<?> doctorGetMedicalRecordsByPatientId(@PathVariable("patientId") Integer patientId) throws JwtValidationException {
//        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
        List<MedicalRecord> medicalRecords = medicalRecordService.listByPatientId(patientId);
        List<MedicalRecordShortDTO> medicalRecordShortDTOS = listEntity2ListDTO(medicalRecords);
        String accessToken = authServiceClient.login(serviceUsername, secretPassword);
        int size = medicalRecordShortDTOS.size();
        for (int i = 0; i < size; i++) {
            medicalRecordShortDTOS.get(i).setDoctorName(authServiceClient.getUserInfo(medicalRecordShortDTOS.get(i).getDoctorId(), accessToken).getHoten());
        }
        return ResponseEntity.ok(medicalRecordShortDTOS);
    }
    @GetMapping("/records/{id}")
    public ResponseEntity<?> medicalRecordDetails(@PathVariable("id") Integer medicalRecordId) throws JwtValidationException {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(medicalRecordId);
        String accessToken = authServiceClient.login(serviceUsername, secretPassword);
        UserInfoDTO userInfoDTO = authServiceClient.getUserInfo(medicalRecord.getDoctorId(), accessToken);
        MedicalRecordFullDTO medicalRecordfullDTO = entity2fullDTO(medicalRecord);
        medicalRecordfullDTO.setDoctorName(userInfoDTO.getHoten());
        if(medicalRecordfullDTO.getExaminations() != null) {
            for (ExaminationDTO examination : medicalRecordfullDTO.getExaminations()) {
                examination.setDoctorName(authServiceClient.getUserInfo(examination.getDoctorId(), accessToken).getHoten());
            }
        }
        return ResponseEntity.ok(medicalRecordfullDTO);
    }
    @GetMapping("/records/created/recent/patients")
    public ResponseEntity<?> getPatientsWithRecentMedicalRecordsCreatedByDoctor(@RequestParam(value = "limit", required = false, defaultValue = "6") Integer limit) throws JwtValidationException {
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Integer doctorId = user.getUserId();
        List<RecentPatientsDTO> patientIds = medicalRecordService.getRecentMedicalRecordsCreatedPatientsByDoctorId(doctorId, limit);
        String accessToken = null;
        if(!patientIds.isEmpty()){
            accessToken = authServiceClient.login(serviceUsername, secretPassword);
        }
        List<UserInfoDTO> userInfoDTOS = new ArrayList<>();
        for (int i = 0; i < patientIds.size(); i++) {
            userInfoDTOS.add(authServiceClient.getUserInfo(patientIds.get(i).getUserId(),accessToken));
            userInfoDTOS.get(i).setMedicalRecordId(patientIds.get(i).getMedicalRecordId());
        }
        return ResponseEntity.ok(userInfoDTOS);
    }
    @GetMapping("/examinations/created/recent/patients")
    public ResponseEntity<?> getPatientsWithRecentExaminationsCreatedByDoctor(@RequestParam(value = "limit", required = false, defaultValue = "6") Integer limit) throws JwtValidationException {
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Integer doctorId = user.getUserId();
        List<RecentPatientsDTO> patientIds = examinationService.getRecentExaminationsCreatedPatientsByDoctorId(doctorId, limit);
        String accessToken = null;
        if(!patientIds.isEmpty()){
            accessToken = authServiceClient.login(serviceUsername, secretPassword);
        }
        List<UserInfoDTO> userInfoDTOS = new ArrayList<>();
        for (int i = 0; i < patientIds.size(); i++) {
            userInfoDTOS.add(authServiceClient.getUserInfo(patientIds.get(i).getUserId(),accessToken));
            userInfoDTOS.get(i).setMedicalRecordId(patientIds.get(i).getMedicalRecordId());
            userInfoDTOS.get(i).setExaminationId(patientIds.get(i).getExaminationId());
        }
        return ResponseEntity.ok(userInfoDTOS);
    }
    @GetMapping("/patients/search")
    public ResponseEntity<?> searchPatient(@RequestParam(value = "id", required = false) Integer patientId, @RequestParam(value = "fullName", required = false) String patientFullName) throws JwtValidationException {
        String accessToken = authServiceClient.login(serviceUsername, secretPassword);
        List<UserInfoDTO> userInfoDTOS = authServiceClient.searchPatientInfo(patientId, patientFullName, accessToken);
        System.out.println(patientId);
        System.out.println(patientFullName);
        return ResponseEntity.ok(userInfoDTOS);
    }
    @GetMapping("/prescription/details/{id}")
    public ResponseEntity<?> prescriptionDetails(@PathVariable("id") Integer examinationId, @RequestParam(value = "patientId", required = false) Integer patientIdParam) throws JwtValidationException {
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        int patientId = 0;
        if(patientIdParam != null){
            patientId = patientIdParam;
        }
        else{
            patientId = user.getUserId();
        }
        int examId = examinationId;
        Prescription prescription = prescriptionService.getPrescription(patientId, examId);
        List<PrescriptionDetail> prescriptionDetail = prescription.getPrescriptionDetails();
        String accessToken = authServiceClient.login(serviceUsername, secretPassword);
        UserInfoDTO userInfoDTO = authServiceClient.getUserInfo(patientId, accessToken);
        PrescriptionDetailDTO prescriptionDetailDTO = new PrescriptionDetailDTO();
        prescriptionDetailDTO.setMedicines(new java.util.ArrayList<>());
        prescriptionDetailDTO.setPatientId(patientId);
        prescriptionDetailDTO.setPatientName(userInfoDTO.getHoten());
        prescriptionDetailDTO.setCreatedAt(prescription.getCreatedAt());
        int doctorId = prescription.getExamination().getDoctorId();
        prescriptionDetailDTO.setDoctorId(doctorId);
        UserInfoDTO doctorInfoDTO = authServiceClient.getUserInfo(doctorId, accessToken);
        prescriptionDetailDTO.setDoctorName(doctorInfoDTO.getHoten());
        prescriptionDetailDTO.setStatus(prescription.getStatus());
        double totalPrice = 0;
        for (PrescriptionDetail prescriptionDetail1 : prescriptionDetail) {
            totalPrice += prescriptionDetail1.getQuantity() * prescriptionDetail1.getMedicine().getPrice();
            MedicineDTO medicineDTO = new MedicineDTO();
            medicineDTO.setMedicineId(prescriptionDetail1.getMedicine().getMedicineId());
            medicineDTO.setMedicineName(prescriptionDetail1.getMedicine().getMedicineName());
            medicineDTO.setQuantity(prescriptionDetail1.getQuantity());
            medicineDTO.setPrice(prescriptionDetail1.getMedicine().getPrice());
            medicineDTO.setTotalPrice(prescriptionDetail1.getQuantity() * prescriptionDetail1.getMedicine().getPrice());
            prescriptionDetailDTO.getMedicines().add(medicineDTO);
        }
        prescriptionDetailDTO.setTotalPrice(totalPrice);
        return ResponseEntity.ok(prescriptionDetailDTO);
    }

    private MedicalRecordShortDTO entity2DTO(MedicalRecord entity) {
        return modelMapper.map(entity, MedicalRecordShortDTO.class);
    }
    private MedicalRecordFullDTO entity2fullDTO(MedicalRecord entity) {
        return modelMapper.map(entity, MedicalRecordFullDTO.class);
    }
    private List<MedicalRecordShortDTO> listEntity2ListDTO(List<MedicalRecord> listEntity) {

        return listEntity.stream().map(entity -> entity2DTO(entity))
                .collect(Collectors.toList());

    }

}
