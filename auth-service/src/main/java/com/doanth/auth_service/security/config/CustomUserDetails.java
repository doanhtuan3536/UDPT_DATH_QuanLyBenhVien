package com.doanth.auth_service.security.config;


import com.doanth.auth_service.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    public User getUser() {
        return this.user;
    }
    @Override
    public boolean isAccountNonExpired() {
        return this.user.getStatus().equalsIgnoreCase("active");
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getStatus().equalsIgnoreCase("active");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.getStatus().equalsIgnoreCase("active");
    }

    @Override
    public boolean isEnabled() {
        return this.user.getStatus().equalsIgnoreCase("active");
    }
}
