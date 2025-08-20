package com.doanth.qlbv_web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final RemoteAuthProvider remoteAuthProvider;

    public SecurityConfig(RemoteAuthProvider remoteAuthProvider) {
        this.remoteAuthProvider = remoteAuthProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // disable only for testing APIs
                .authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/css/**", "/js/**", "/assets/**", "/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // your custom login page
                        .loginProcessingUrl("/login") // POST action in your form
                        .failureUrl("/login?error=true") // redirect on failure
                        .defaultSuccessUrl("/home", true)
                        .permitAll()) // default login page
                .logout(logout -> logout.permitAll())
                .authenticationProvider(remoteAuthProvider);

        return http.build();
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    }
}
