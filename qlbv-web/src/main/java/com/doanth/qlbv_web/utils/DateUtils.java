package com.doanth.qlbv_web.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DateUtils {
    public static String getVietnameseDayOfWeek(LocalDate date) {
        String[] days = {"Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"};
        return days[date.getDayOfWeek().getValue() % 7];
    }
    public static boolean isAppointmentInFuture(LocalDate appointmentDate, LocalTime appointmentTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
        return appointmentDateTime.isAfter(now);
    }
}
