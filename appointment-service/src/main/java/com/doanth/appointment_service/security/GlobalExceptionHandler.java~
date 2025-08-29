package com.doanth.appointment_service.security;

import com.doanth.appointment_service.service.AppointmentNotFoundException;
import com.doanth.appointment_service.service.AppointmentTimeNotBetweenWorkingHoursException;
import com.doanth.appointment_service.service.SpecialtyNotFoundException;
import com.doanth.appointment_service.service.SpecialtyWorkDaysException;
import com.doanth.appointment_service.serviceclient.AccessTokenForServiceException;
import com.doanth.appointment_service.serviceclient.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JwtValidationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handleJwtValidationException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.addError(ex.getMessage());
        error.setPath(request.getServletPath());
        return error;
    }

    @ExceptionHandler({SpecialtyNotFoundException.class, SpecialtyWorkDaysException.class, AppointmentTimeNotBetweenWorkingHoursException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleMakeAppointmentException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.addError(ex.getMessage());
        error.setPath(request.getServletPath());
        return error;
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleAppointmentNotFoundException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.addError(ex.getMessage());
        error.setPath(request.getServletPath());
        return error;
    }

    @ExceptionHandler({AccessTokenForServiceException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleAccessTokenForServiceException(HttpServletRequest request, Exception ex) {
        ErrorDTO error = new ErrorDTO();

        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.addError(ex.getMessage());
        error.setPath(request.getServletPath());
        return error;
    }
}
