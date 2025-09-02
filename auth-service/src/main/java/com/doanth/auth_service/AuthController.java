package com.doanth.auth_service;

import com.doanth.auth_service.dto.LoginForm;
import com.doanth.auth_service.dto.SignupForm;
import com.doanth.auth_service.dto.SignupResult;
import com.doanth.auth_service.model.User;
import com.doanth.auth_service.security.auth.AuthResponse;
import com.doanth.auth_service.security.auth.TokenService;
import com.doanth.auth_service.security.config.CustomServiceDetails;
import com.doanth.auth_service.security.config.CustomUserDetails;
import com.doanth.auth_service.security.jwt.JwtUtility;
import com.doanth.auth_service.security.jwt.JwtValidationException;
import com.doanth.auth_service.security.refreshtoken.RefreshTokenExpiredException;
import com.doanth.auth_service.security.refreshtoken.RefreshTokenNotFoundException;
import com.doanth.auth_service.security.refreshtoken.RefreshTokenRequest;
import com.doanth.auth_service.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  UserService userService;
    @Autowired
    @Qualifier("userAuthManager")
    private  AuthenticationManager userAuthManager;
    @Autowired
    @Qualifier("serviceAuthManager")
    private AuthenticationManager serviceAuthManager;
//    private final JwtUtility jwtUtil;
    @Autowired
    private  TokenService tokenService;
    @Autowired
    private  JwtUtility jwtUtil;

//    public AuthController(PasswordEncoder passwordEncoder, UserService userService,
//                          AuthenticationManager authenticationManager, TokenService tokenService, JwtUtility jwtUtil) {
//        this.authenticationManager = authenticationManager;
//        this.passwordEncoder = passwordEncoder;
//        this.userService = userService;
////        this.jwtUtil = jwtUtil;
//        this.tokenService = tokenService;
//        this.jwtUtil = jwtUtil;
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupForm body) {
        SignupResult signupResult = new SignupResult();
        System.out.println(body);
        if(userService.emailExists(body.getEmail()))
        {
            signupResult.setStatus("error");
            signupResult.setMessage("Email đã tồn tại");
            signupResult.setEmail(body.getEmail());
            return ResponseEntity.badRequest().body(signupResult);
        }
        if (userService.usernameExists(body.getUsername())) {
            signupResult.setStatus("error");
            signupResult.setMessage("Tên đăng nhập đã tồn tại");
            signupResult.setEmail(body.getEmail());
            return ResponseEntity.badRequest().body(signupResult);
        }
        try {
            User newUser = new User();
            newUser.setUsername(body.getUsername());
            newUser.setPassword(passwordEncoder.encode(body.getPassword()));
            newUser.setEmail(body.getEmail());
            newUser.setGioitinh(body.getGioitinh());
            newUser.setHoten(body.getFullName());
            newUser.setNgaysinh(body.getNgaysinh());
            newUser.setStatus("active");
            newUser.setLoai("benhnhan");
            newUser.setSdt(body.getSdt());

            userService.saveUser(newUser);

            signupResult.setStatus("success");
            signupResult.setMessage("Tạo tài khoản thành công");
            signupResult.setEmail(body.getEmail());
            signupResult.setFullName(body.getFullName());
            signupResult.setUserName(body.getUsername());
            signupResult.setSdt(body.getSdt());
            return ResponseEntity.ok(signupResult);
        }
        catch (Exception e) {
            e.printStackTrace();
            signupResult.setStatus("error");
            signupResult.setMessage("Tạo tài khoản thất bại do lỗi hệ thống, vui lòng thử lại sau");
            signupResult.setEmail(body.getEmail());;
        }
        return ResponseEntity.badRequest().body(signupResult);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm body) {
        try {
            String username = body.getUsername();
            String password = body.getPassword();

            Authentication authentication = userAuthManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            AuthResponse token = tokenService.generateTokens(customUserDetails.getUser());
            token.setRole(customUserDetails.getAuthorities().iterator().next().getAuthority());
            token.setHoten(customUserDetails.getUser().getHoten());
            System.out.println(token);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/service/login")
    public ResponseEntity<?> serviceLogin(@RequestBody LoginForm body) {
        try {
            String username = body.getUsername();
            String password = body.getPassword();

            Authentication authentication = serviceAuthManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            CustomServiceDetails customUserDetails = (CustomServiceDetails) authentication.getPrincipal();

            String token = tokenService.generateTokenForService(customUserDetails.getService());
//            token.setRole(customUserDetails.getAuthorities().iterator().next().getAuthority());
//            token.setHoten(customUserDetails.getUser().getHoten());
            System.out.println("serviceLogin");
            System.out.println(token);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            AuthResponse response = tokenService.refreshTokens(request);
            return ResponseEntity.ok(response);

        } catch (RefreshTokenNotFoundException | RefreshTokenExpiredException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/token/validate")
    public ResponseEntity<?> validateAccessToken(@RequestBody String AccessTokenJwt) {
        System.out.println(AccessTokenJwt);
        try {
            Claims claims = jwtUtil.validateAccessToken(AccessTokenJwt);
            System.out.println(claims);
            return ResponseEntity.ok(claims);

        } catch (JwtValidationException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/token")
    public ResponseEntity<?> getNewToken(@RequestBody LoginForm body) {
        try {
            String username = body.getUsername();
            String password = body.getPassword();

            Authentication authentication = userAuthManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            AuthResponse token = tokenService.generateTokens(customUserDetails.getUser());
            token.setRole(customUserDetails.getAuthorities().iterator().next().getAuthority());
            token.setHoten(customUserDetails.getUser().getHoten());
            System.out.println(token);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Integer userId) {
        User user = userService.get(userId);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/user/patient")
    public ResponseEntity<?> getPatientInfoByPatientIdAndPatientFullName(@RequestParam(value = "id", required = false, defaultValue = "0") Integer userId, @RequestParam(value = "fullName", required = false) String userFullName) {
        List<User> users = new ArrayList<>();
        System.out.println(userId);
        System.out.println(userFullName);
        if (userId == null && userFullName == null) return ResponseEntity.ok(users);
        if(userFullName != null && !userFullName.isEmpty() && userId != null && userId > 0)
        {
            return ResponseEntity.ok(userService.SearchPatientByPatientFullNameAndPatientId(userFullName, userId));
        }
        if(userId != null && userId > 0){
            users.addAll(userService.SearchPatientByPatientId(userId));
        }
        if(userFullName != null && !userFullName.isEmpty()){
            users.addAll(userService.SearchPatientByPatientFullName(userFullName));
        }
        return ResponseEntity.ok(users);
    }


}
