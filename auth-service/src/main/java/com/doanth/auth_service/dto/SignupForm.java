package com.doanth.auth_service.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class SignupForm {
        @NotBlank(message = "Tên đăng nhập không được để trống")
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        private String email;

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
        private String password;

        @NotBlank(message = "Vui lòng xác nhận mật khẩu")
        private String confirmPassword;

        @NotBlank(message = "Họ và tên không được để trống")
        private String fullName;

        @AssertTrue(message = "Bạn phải đồng ý với điều khoản sử dụng")
        private boolean agreeTerms;

        private LocalDate ngaysinh;

        private String gioitinh;

        @NotBlank(message = "Số điện thoại không được để trống")
        private String sdt;

        public LocalDate getNgaysinh() {
                return ngaysinh;
        }

        public void setNgaysinh(LocalDate ngaysinh) {
                this.ngaysinh = ngaysinh;
        }

        public String getGioitinh() {
                return gioitinh;
        }

        public void setGioitinh(String gioitinh) {
                this.gioitinh = gioitinh;
        }

        public @NotBlank(message = "Số điện thoại không được để trống") String getSdt() {
                return sdt;
        }

        public void setSdt(@NotBlank(message = "Số điện thoại không được để trống")  String sdt) {
                this.sdt = sdt;
        }

        public @NotBlank(message = "Tên đăng nhập không được để trống") @Size(min = 3, max = 50) String getUsername() {
                return username;
        }

        public void setUsername(@NotBlank(message = "Tên đăng nhập không được để trống") @Size(min = 3, max = 50) String username) {
                this.username = username;
        }

        public @NotBlank(message = "Email không được để trống") @Email(message = "Email không hợp lệ") String getEmail() {
                return email;
        }

        public void setEmail(@NotBlank(message = "Email không được để trống") @Email(message = "Email không hợp lệ") String email) {
                this.email = email;
        }

        public @NotBlank(message = "Mật khẩu không được để trống") @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự") String getPassword() {
                return password;
        }

        public void setPassword(@NotBlank(message = "Mật khẩu không được để trống") @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự") String password) {
                this.password = password;
        }

        public @NotBlank(message = "Vui lòng xác nhận mật khẩu") String getConfirmPassword() {
                return confirmPassword;
        }

        public void setConfirmPassword(@NotBlank(message = "Vui lòng xác nhận mật khẩu") String confirmPassword) {
                this.confirmPassword = confirmPassword;
        }

        public @NotBlank(message = "Họ và tên không được để trống") String getFullName() {
                return fullName;
        }

        public void setFullName(@NotBlank(message = "Họ và tên không được để trống") String fullName) {
                this.fullName = fullName;
        }

        @AssertTrue(message = "Bạn phải đồng ý với điều khoản sử dụng")
        public boolean isAgreeTerms() {
                return agreeTerms;
        }

        public void setAgreeTerms(@AssertTrue(message = "Bạn phải đồng ý với điều khoản sử dụng") boolean agreeTerms) {
                this.agreeTerms = agreeTerms;
        }

        @Override
        public String toString() {
                return "SignupForm{" +
                        "username='" + username + '\'' +
                        ", email='" + email + '\'' +
                        ", password='" + password + '\'' +
                        ", confirmPassword='" + confirmPassword + '\'' +
                        ", fullName='" + fullName + '\'' +
                        ", agreeTerms=" + agreeTerms +
                        ", ngaysinh=" + ngaysinh +
                        ", gioitinh='" + gioitinh + '\'' +
                        ", sdt='" + sdt + '\'' +
                        '}';
        }
}
