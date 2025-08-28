package com.doanth.qlbv_web.controllers;

import com.doanth.qlbv_web.dto.*;
import com.doanth.qlbv_web.rabbitmq.AppointmentConfirmNotificationProducer;
import com.doanth.qlbv_web.serviceClient.*;
import com.doanth.qlbv_web.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${service.auth.username}")
    private String serviceUsername;

    @Value("${service.auth.password}")
    private String secretPassword;
    private final AppointmentServiceClient appointmentServiceClient;
    private final AuthServiceClient authServiceClient;
    private int pageNumForResolved = 1;
    private int pageNumForNotResolved = 1;
    private final DateUtils dateUtils;
    private final AppointmentConfirmNotificationProducer producer;

    public AppointmentController(AppointmentServiceClient appointmentServiceClient, DateUtils dateUtils, AuthServiceClient authServiceClient, AppointmentConfirmNotificationProducer producer) {
        this.appointmentServiceClient = appointmentServiceClient;
        this.dateUtils = dateUtils;
        this.authServiceClient = authServiceClient;
        this.producer = producer;
    }

    @GetMapping
    public String listAppointments(Model model, @RequestParam(value = "pageForNotResolved", required = false, defaultValue = "-1")
                                                @Min(value = -1)	Integer pageForNotResolved,
                                   @RequestParam(value = "SizeForNotResolved", required = false, defaultValue = "5")
                                       @Min(value = 5) @Max(value = 20) Integer pageSizeForNotResolved,
                                   @RequestParam(value = "pageForResolved", required = false, defaultValue = "-1")
                                       @Min(value = -1)	Integer pageForResolved,
                                   @RequestParam(value = "SizeForResolved", required = false, defaultValue = "5")
                                       @Min(value = 5) @Max(value = 20) Integer pageSizeForResolved) throws RefreshTokenException, JwtValidationException, JsonProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();
        String accessToken = loggedUser.getAccessToken();
        if(pageForNotResolved != -1) {
            pageNumForNotResolved = pageForNotResolved;
        }
        if(pageForResolved != -1) {
            pageNumForResolved = pageForResolved;
        }
        ListAppointmentInfo listAppointmentInfoResolved = appointmentServiceClient.listAppointments(accessToken, "resolved", pageNumForResolved, pageSizeForResolved);
        System.out.println(listAppointmentInfoResolved);
        ListAppointmentInfo listAppointmentInfoNotResolved = appointmentServiceClient.listAppointments(accessToken, "-resolved", pageNumForNotResolved, pageSizeForNotResolved);
        System.out.println(listAppointmentInfoNotResolved);
        // Thêm dữ liệu vào model
        model.addAttribute("resolvedAppointments", listAppointmentInfoResolved.getListAppointments());
        model.addAttribute("notResolvedAppointments", listAppointmentInfoNotResolved.getListAppointments());
        model.addAttribute("resolvedPageMetadata", listAppointmentInfoResolved.getPageMetadata());
        model.addAttribute("notResolvedPageMetadata", listAppointmentInfoNotResolved.getPageMetadata());
        model.addAttribute("currentPageResolved", pageNumForResolved);
        model.addAttribute("currentPageNotResolved", pageNumForNotResolved);
        model.addAttribute("pageSizeResolved", pageSizeForResolved);
        model.addAttribute("pageSizeNotResolved", pageSizeForNotResolved);
        model.addAttribute("dateUtils", dateUtils);
        model.addAttribute("listDoctorsForResolved", listAppointmentInfoResolved.getListDoctors());

        return "appointment_list";
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

    @GetMapping("/no-auth/add")
    public String addAppointmentNoAuth(Model model) throws RefreshTokenException, JwtValidationException {
        String accessToken = authServiceClient.serviceLogin(serviceUsername, secretPassword);
        List<Specialty> specialties = appointmentServiceClient.getSpecialties(accessToken);
        System.out.println(specialties);
        model.addAttribute("specialties", specialties);
//        model.addAttribute("bookingForm", new BookingForm());
//        if (!model.containsAttribute("bookingForm")) {
//            model.addAttribute("bookingForm", new AppointmentInfo());
//        }
        return "appointments_no_auth";
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

    @GetMapping("/confirm/{id}")
    public String confirmAppointment(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) throws RefreshTokenException, JwtValidationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();
        AppointmentInfo appointmentInfo = appointmentServiceClient.confirmAppointment(loggedUser.getAccessToken(), id);
        String accessToken = authServiceClient.serviceLogin(serviceUsername, secretPassword);
        UserDetailsFromDB userDetailsFromDB = authServiceClient.getuserInfo(appointmentInfo.getUserId(), accessToken);
        appointmentInfo.setUserEmail(userDetailsFromDB.getEmail());
        producer.sendAppointmentConfirmMessage(appointmentInfo);

        redirectAttributes.addFlashAttribute("successfulMessage", "Xác nhận lịch khám thành công");

        System.out.println(appointmentInfo);

        return "redirect:/appointments";
    }

    @GetMapping("/notconfirm/{id}")
    public String notconfirmAppointment(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) throws RefreshTokenException, JwtValidationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();
        AppointmentInfo appointmentInfo = appointmentServiceClient.notconfirmAppointment(loggedUser.getAccessToken(), id);
        String accessToken = authServiceClient.serviceLogin(serviceUsername, secretPassword);
        UserDetailsFromDB userDetailsFromDB = authServiceClient.getuserInfo(appointmentInfo.getUserId(), accessToken);
        appointmentInfo.setUserEmail(userDetailsFromDB.getEmail());
        producer.sendAppointmentNotConfirmMessage(appointmentInfo);
        redirectAttributes.addFlashAttribute("successfulMessage", "Hủy xác nhận lịch khám thành công");
        System.out.println(appointmentInfo);

        return "redirect:/appointments";
    }
    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) throws RefreshTokenException, JwtValidationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AuthResponse loggedUser = ((UserDetails) auth.getPrincipal()).getAuthResponse();
        AppointmentInfo appointmentInfo = appointmentServiceClient.deleteAppointment(loggedUser.getAccessToken(), id);
        redirectAttributes.addFlashAttribute("successfulDeleteMessage", "Hủy lịch khám thành công");
        System.out.println(appointmentInfo);

        return "redirect:/appointments";
    }
}
