package com.doanth.qlbv_web.controllers;

import com.doanth.qlbv_web.dto.AppointmentInfo;
import com.doanth.qlbv_web.dto.Specialty;
import com.doanth.qlbv_web.dto.UserDetails;
import com.doanth.qlbv_web.serviceClient.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentServiceClient appointmentServiceClient;

    public AppointmentController(AppointmentServiceClient appointmentServiceClient) {
        this.appointmentServiceClient = appointmentServiceClient;
    }

    @GetMapping("/add")
    public String addAppointmentForm(Model model) throws RefreshTokenException, JwtValidationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();
        System.out.println(auth);
        System.out.println("addAppointment");
        String accessToken = loggedUser.getAccessToken();
//        List<String>
        List<Specialty> specialties = appointmentServiceClient.getSpecialties(accessToken);
        System.out.println(specialties);
        model.addAttribute("specialties", specialties);
//        model.addAttribute("bookingForm", new BookingForm());
        if (!model.containsAttribute("bookingForm")) {
            model.addAttribute("bookingForm", new AppointmentInfo());
        }
        return "appointment_add";
    }

    @PostMapping("/add")
    public String addAppointment(@ModelAttribute("bookingForm") AppointmentInfo form , @RequestParam("hour") int hour,
                                 @RequestParam("minute") int minute, RedirectAttributes redirectAttributes) throws RefreshTokenException, JwtValidationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();

        form.setUserId(loggedUser.getUserId());
        form.setAppointmentTime(LocalTime.of(hour, minute));
        System.out.println(form);
        redirectAttributes.addFlashAttribute("bookingForm", form);
        try {
            appointmentServiceClient.makeAppointment(form, loggedUser.getAccessToken());
            redirectAttributes.addFlashAttribute("successfulMessage", "Đặt lịch khám thành công, bạn hãy chờ bác sĩ xác nhận để đến khám nhé");
        } catch (SpecialtyNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chuyên khoa này hiện tại không tồn tại, hoặc lỗi hệ thống");
        } catch (AppointmentTimeNotBetweenWorkingHoursException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giờ khám ngoài giờ làm việc của chuyên khoa");
        } catch (SpecialtyWorkDaysException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chuyên khoa bạn chọn không làm việc vào ngày " +
                    form.getAppointmentDate() + ", vui lòng chọn ngày khác phù hợp với lịch làm việc");
        }


        return "redirect:/appointments/add";
    }
}
