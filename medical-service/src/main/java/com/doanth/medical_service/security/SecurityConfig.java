package com.doanth.medical_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    JwtTokenFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.GET,"/api/medical/records/*")
                                .hasAnyAuthority("benhnhan", "bacsi", "nhanvien")
                                .requestMatchers(HttpMethod.GET,"/api/medical/doctor/records/*")
                                .hasAnyAuthority("bacsi", "nhanvien")
                                .requestMatchers(HttpMethod.GET,"/api/medical/records")
                                .hasAnyAuthority("benhnhan", "bacsi", "nhanvien")
                                .requestMatchers(HttpMethod.GET,"/api/medical/prescription/details/*")
                                .hasAnyAuthority("benhnhan", "bacsi", "nhanvien")
                                .requestMatchers(HttpMethod.GET,"/api/medical/patients/search")
                                .hasAnyAuthority("bacsi", "nhanvien")
                                .requestMatchers(HttpMethod.GET,"/api/medical/records/created/recent/patients")
                                .hasAnyAuthority("bacsi", "nhanvien")
                                .requestMatchers(HttpMethod.GET,"/api/medical/examinations/created/recent/patients")
                                .hasAnyAuthority("bacsi", "nhanvien")
//                        .requestMatchers(HttpMethod.POST,"/api/appointments")
//                                .hasAnyAuthority("benhnhan", "nhanvien")
//                                .requestMatchers(HttpMethod.GET,"/api/appointments")
//                                .hasAnyAuthority("benhnhan", "bacsi")
//                                .requestMatchers(HttpMethod.PUT,"/api/appointments/confirm/*")
//                                .hasAnyAuthority("nhanvien", "bacsi")
//                                .requestMatchers(HttpMethod.PUT,"/api/appointments/notconfirm/*")
//                                .hasAnyAuthority("nhanvien", "bacsi")
//                                .requestMatchers(HttpMethod.DELETE,"/api/appointments/delete/*")
//                                .hasAnyAuthority("nhanvien", "bacsi", "benhnhan")
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
