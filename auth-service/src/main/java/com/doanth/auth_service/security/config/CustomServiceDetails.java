package com.doanth.auth_service.security.config;

import com.doanth.auth_service.model.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomServiceDetails implements org.springframework.security.core.userdetails.UserDetails {
    private final Service service;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomServiceDetails(Service service, Collection<? extends GrantedAuthority> authorities) {
        this.service = service;
        this.authorities = authorities;
    }

    public Service getService() {
        return service;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return service.getServicePassword();
    }

    @Override
    public String getUsername() {
        return service.getServiceName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.service.getStatus().equalsIgnoreCase("active");
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.service.getStatus().equalsIgnoreCase("active");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.service.getStatus().equalsIgnoreCase("active");
    }

    @Override
    public boolean isEnabled() {
        return this.service.getStatus().equalsIgnoreCase("active");
    }
}
