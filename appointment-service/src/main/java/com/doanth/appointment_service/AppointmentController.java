package com.doanth.appointment_service;

import com.doanth.appointment_service.dto.AppointmentInfoDTO;
import com.doanth.appointment_service.dto.SpecialtyInforForListAppointments;
import com.doanth.appointment_service.models.Appointment;
import com.doanth.appointment_service.models.Specialty;
import com.doanth.appointment_service.security.JwtValidationException;
import com.doanth.appointment_service.security.User;
import com.doanth.appointment_service.service.*;
import com.doanth.appointment_service.serviceclient.AuthServiceClient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/appointments")
@Validated
public class AppointmentController {
    @Autowired
    private SpecialtyService specialtyService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthServiceClient authServiceClient;

    @Value("${service.auth.username}")
    private String serviceUsername;

    @Value("${service.auth.password}")
    private String secretPassword;

    @GetMapping("/specialties")
    public ResponseEntity<?> listSpecialties() {
//        Specialty specialty = new Specialty();
//        specialty.setSpecialtyId(1);
//        specialty.setName("Chuyen Khoa 1");
//        specialty.setDaysWorkInWeek("Thứ hai, Thứ ba");
//        specialty.setStartTime(LocalTime.of(8, 0, 0));
//        specialty.setEndTime(LocalTime.of(17, 0, 0));
        List<Specialty> specialties = specialtyService.list(); // <1>
        return ResponseEntity.ok(specialties);
    }

    @GetMapping
    public ResponseEntity<?> listAppointments(@RequestParam(value = "page", required = false, defaultValue = "1")
                                                  @Min(value = 1)	Integer page,

                                              @RequestParam(value = "size", required = false, defaultValue = "5")
                                                  @Min(value = 5) @Max(value = 20) Integer pageSize,

                                              @RequestParam(value = "sort", required = false, defaultValue = "-appointmentDate,-appointmentTime,appointmentId") String sortOption,
                                              @RequestParam(value = "status", required = false, defaultValue = "resolved") String status) throws BadRequestException, JwtValidationException {
//        List<Specialty> specialties = specialtyService.list();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String role = user.getAuthorities().iterator().next().getAuthority();
        if(role.equals("bacsi")) {
            Integer specialtyId = doctorService.getSpecialtyIdByDoctorId(user.getUserId());
            SpecialtyInforForListAppointments specialty = specialtyService.getNameRoomBySpecialtyId(specialtyId);

            Map<String, Object> filterFields = new java.util.HashMap<>();
            filterFields.put("specialty.specialtyId", specialtyId);
            Page<Appointment> appointments = null;
            if(status.startsWith("-")) {
                filterFields.put("ne_status", status.substring(1));

                appointments = appointmentService.listByPage(page-1, pageSize, sortOption, filterFields);
            }
            else {
                filterFields.put("status", status);
                filterFields.put("doctor.doctorId", user.getUserId());
                appointments = appointmentService.listByPage(page - 1, pageSize, sortOption, filterFields);
            }
            System.out.println(appointments);

            List<AppointmentInfoDTO> listAppointmentsDTO = listEntity2ListDTO(appointments.getContent());
//            List<AppointmentInfoDTO> listAppointmentsResolved = listEntity2ListDTO(resolvedAppointments);
            listAppointmentsDTO.forEach(a -> {
                a.setSpecialtyName(specialty.getName());
                a.setSpecialtyRoom(specialty.getRoom());
            });
            var respondBody = addPageMetadataAndLinks2Collection(listAppointmentsDTO, appointments, sortOption, status);
            System.out.println(respondBody);
            return ResponseEntity.ok(respondBody);
        }
        else if(role .equals("benhnhan")) {

            Map<String, Object> filterFields = new java.util.HashMap<>();
            filterFields.put("userId", user.getUserId());
            Page<Appointment> appointments = null;
            if(status.startsWith("-")) {
                filterFields.put("ne_status", status.substring(1));

                appointments = appointmentService.listByPage(page-1, pageSize, sortOption, filterFields);
            }
            else {
                filterFields.put("status", status);
//                filterFields.put("doctor.doctorId", user.getUserId());
                appointments = appointmentService.listByPage(page - 1, pageSize, sortOption, filterFields);
            }
            List<Appointment> listAppointments = appointments.getContent();
            List<AppointmentInfoDTO> listAppointmentsDTO = listEntity2ListDTO(listAppointments);
            String accessToken = authServiceClient.login(serviceUsername, secretPassword);
            for (int i = 0; i < listAppointmentsDTO.size(); i++) {
                listAppointmentsDTO.get(i).setSpecialtyName(listAppointments.get(i).getSpecialty().getName());
                listAppointmentsDTO.get(i).setSpecialtyRoom(listAppointments.get(i).getSpecialty().getRoom());
                if(status.equals("resolved")){
                    listAppointmentsDTO.get(i).setDoctorInfoDTO(authServiceClient.getDoctorInfo(listAppointments.get(i).getDoctor().getDoctorId(), accessToken));
                    listAppointmentsDTO.get(i).getDoctorInfoDTO().setAppointmentId(listAppointmentsDTO.get(i).getAppointmentId());
                }
            }
//            if(status.equals("resolved")){
//                listAppointmentsDTO.forEach(a -> {
//                    try {
//                        a.setDoctorInfoDTO(authServiceClient.getDoctorInfo(a.getUserId(), accessToken));
//                    } catch (JwtValidationException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            }
            var respondBody = addPageMetadataAndLinks2Collection(listAppointmentsDTO, appointments, sortOption, status);
            System.out.println(respondBody);
            return ResponseEntity.ok(respondBody);

//            DoctorInfoDTO doctorInfoDTO = authServiceClient.getDoctorInfo(user.getUserId(), accessToken);

        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping
    public ResponseEntity<?> addAppointment(@RequestBody AppointmentInfoDTO appointment) {
//        Appointment newAppointment = dto2Entity(appointment);

        Specialty specialty = specialtyService.get(appointment.getSpecialtyId());

        // 1. Convert date → Vietnamese day name
        DayOfWeek dow = appointment.getAppointmentDate().getDayOfWeek();
        String vnDay = convertDayOfWeekToVietnamese(dow);

        // 2. Check if specialty works on that day
        boolean dayOk = specialty.getDaysWorkInWeek().contains(vnDay);

        // 3. Check time range
        LocalTime time = appointment.getAppointmentTime();
        boolean timeOk = !time.isBefore(specialty.getStartTime())
                && !time.isAfter(specialty.getEndTime());

        if (!dayOk) {
            throw new SpecialtyWorkDaysException(vnDay);
        }
        if (!timeOk) {
            throw new AppointmentTimeNotBetweenWorkingHoursException("Appointment time not within working hours");
        }

        Appointment newAppointment = dto2Entity(appointment);
        System.out.println(newAppointment);
        newAppointment.setStatus("pending");
        newAppointment.setTrashed(false);

        Appointment savedAppointment = appointmentService.add(newAppointment);

        return ResponseEntity.ok(entity2DTO(savedAppointment));
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<?> confirmAppointment(@PathVariable("id") Integer appointmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Appointment updatedAppointment = appointmentService.updateStatusById(appointmentId, user.getUserId(), "resolved");

        return ResponseEntity.ok(entity2DTO(updatedAppointment));
    }

    @PutMapping("/notconfirm/{id}")
    public ResponseEntity<?> notconfirmAppointment(@PathVariable("id") Integer appointmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Appointment updatedAppointment = appointmentService.updateStatusById(appointmentId, user.getUserId(), "pending");

        return ResponseEntity.ok(entity2DTO(updatedAppointment));
    }

    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("id") Integer appointmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Appointment deletedAppointment = appointmentService.delete(appointmentId);

        return ResponseEntity.ok(entity2DTO(deletedAppointment));
    }

    private String convertDayOfWeekToVietnamese(DayOfWeek day) {
        switch (day) {
            case MONDAY: return "Thứ Hai";
            case TUESDAY: return "Thứ Ba";
            case WEDNESDAY: return "Thứ Tư";
            case THURSDAY: return "Thứ Năm";
            case FRIDAY: return "Thứ Sáu";
            case SATURDAY: return "Thứ Bảy";
            case SUNDAY: return "Chủ Nhật";
            default: throw new IllegalArgumentException("Invalid day: " + day);
        }
    }

    private PagedModel<AppointmentInfoDTO> addPageMetadataAndLinks2Collection(
            List<AppointmentInfoDTO> listDTO, Page<Appointment> pageInfo, String sortField,
            String status) throws BadRequestException {

        // add self link to each individual item
//        for (AppointmentInfoDTO dto : listDTO) {
//            dto.add(linkTo(methodOn(AppointmentController.class).getLocation(dto.getCode())).withSelfRel());
//        }

        int pageSize = pageInfo.getSize();
        int pageNum = pageInfo.getNumber() + 1;
        long totalElements = pageInfo.getTotalElements();
        int totalPages = pageInfo.getTotalPages();

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageSize, pageNum, totalElements);

        PagedModel<AppointmentInfoDTO> collectionModel = PagedModel.of(listDTO, pageMetadata);

        // add self link to collection
//        collectionModel.add(linkTo(methodOn(AppointmentController.class)
//                .listAppointments(pageNum, pageSize, sortField, status))
//                .withSelfRel());
//
//        if (pageNum > 1) {
//            // add link to first page if the current page is not the first one
//            collectionModel.add(
//                    linkTo(methodOn(AppointmentController.class)
//                            .listAppointments(1, pageSize, sortField, status))
//                            .withRel(IanaLinkRelations.FIRST));
//
//            // add link to the previous page if the current page is not the first one
//            collectionModel.add(
//                    linkTo(methodOn(AppointmentController.class)
//                            .listAppointments(pageNum - 1, pageSize, sortField, status))
//                            .withRel(IanaLinkRelations.PREV));
//        }
//
//        if (pageNum < totalPages) {
//            // add link to next page if the current page is not the last one
//            collectionModel.add(
//                    linkTo(methodOn(AppointmentController.class)
//                            .listAppointments(pageNum + 1, pageSize, sortField, status))
//                            .withRel(IanaLinkRelations.NEXT));
//
//            // add link to last page if the current page is not the last one
//            collectionModel.add(
//                    linkTo(methodOn(AppointmentController.class)
//                            .listAppointments(totalPages, pageSize, sortField, status))
//                            .withRel(IanaLinkRelations.LAST));
//        }


        return collectionModel;

    }


    private AppointmentInfoDTO entity2DTO(Appointment entity) {
        return modelMapper.map(entity, AppointmentInfoDTO.class);
    }

    private Appointment dto2Entity(AppointmentInfoDTO dto) {
        return modelMapper.map(dto, Appointment.class);
    }
    private List<AppointmentInfoDTO> listEntity2ListDTO(List<Appointment> listEntity) {

        return listEntity.stream().map(entity -> entity2DTO(entity))
                .collect(Collectors.toList());

    }
}
