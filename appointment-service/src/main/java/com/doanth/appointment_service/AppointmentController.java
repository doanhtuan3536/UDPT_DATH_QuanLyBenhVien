package com.doanth.appointment_service;

import com.doanth.appointment_service.dto.AppointmentInfoDTO;
import com.doanth.appointment_service.models.Appointment;
import com.doanth.appointment_service.models.Specialty;
import com.doanth.appointment_service.service.AppointmentService;
import com.doanth.appointment_service.service.AppointmentTimeNotBetweenWorkingHoursException;
import com.doanth.appointment_service.service.SpecialtyService;
import com.doanth.appointment_service.service.SpecialtyWorkDaysException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private SpecialtyService specialtyService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private ModelMapper modelMapper;

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

        Appointment savedAppointment = appointmentService.add(newAppointment);

        return ResponseEntity.ok(entity2DTO(savedAppointment));
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

    private AppointmentInfoDTO entity2DTO(Appointment entity) {
        return modelMapper.map(entity, AppointmentInfoDTO.class);
    }

    private Appointment dto2Entity(AppointmentInfoDTO dto) {
        return modelMapper.map(dto, Appointment.class);
    }
}
