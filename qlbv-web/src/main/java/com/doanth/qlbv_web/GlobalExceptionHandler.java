package com.doanth.qlbv_web;

import com.doanth.qlbv_web.serviceClient.JwtValidationException;
import com.doanth.qlbv_web.serviceClient.RefreshTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRefreshTokenException(HttpServletRequest request, Exception ex, Model model) {
//        ErrorDTO error = new ErrorDTO();
//
//        error.setTimestamp(new Date());
//        error.setStatus(HttpStatus.BAD_REQUEST.value());
//        error.addError(ex.getMessage());
//        error.setPath(request.getServletPath());
//
//        LOGGER.error(ex.getMessage(), ex);
        // Xóa thông tin user hiện tại
        SecurityContextHolder.clearContext();

        // Nếu dùng HttpSession thì huỷ session luôn
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        model.addAttribute("errorMessage", "Bạn đã hết hạn đăng nhập. Vui lòng đăng nhập lại để tiếp tục sử dụng hệ thống.");
//        SecurityContextHolder.clearContext();
        return "error";
    }
    @ExceptionHandler(JwtValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolation(HttpServletRequest request, Exception ex, Model model) {
//        ErrorDTO error = new ErrorDTO();
//
//        error.setTimestamp(new Date());
//        error.setStatus(HttpStatus.BAD_REQUEST.value());
//        error.addError(ex.getMessage());
//        error.setPath(request.getServletPath());
//
//        LOGGER.error(ex.getMessage(), ex);
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        model.addAttribute("errorMessage", "Lỗi hệ thống, vui lòng đăng nhập lại");
//        SecurityContextHolder.clearContext();
        return "error";
    }

}
