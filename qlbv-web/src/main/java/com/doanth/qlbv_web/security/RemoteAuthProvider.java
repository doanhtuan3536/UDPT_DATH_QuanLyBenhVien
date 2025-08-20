package com.doanth.qlbv_web.security;

import com.doanth.qlbv_web.serviceClient.AuthResponse;
import com.doanth.qlbv_web.serviceClient.AuthServiceClient;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class RemoteAuthProvider implements AuthenticationProvider {

    private final AuthServiceClient authServiceClient;

    public RemoteAuthProvider(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (username == null || password == null) {
            throw new BadCredentialsException("Đăng nhập thất bại");
        }

        if (username.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("Đăng nhập thất bại");
        }

        try {
            AuthResponse respone = authServiceClient.login(username, password);
            System.out.println(respone);
            // Tạo principal (có thể parse token để lấy roles)
            return new UsernamePasswordAuthenticationToken(
                    username, respone, List.of(new SimpleGrantedAuthority(respone.getRole())));
        } catch (Exception e) {
            throw new BadCredentialsException("Đăng nhập thất bại", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
