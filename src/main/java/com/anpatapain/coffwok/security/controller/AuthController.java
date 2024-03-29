package com.anpatapain.coffwok.security.controller;

import com.anpatapain.coffwok.common.payload.request.LoginRequest;
import com.anpatapain.coffwok.common.payload.request.SignupRequest;
import com.anpatapain.coffwok.common.payload.response.ApiResponse;
import com.anpatapain.coffwok.common.payload.response.AuthResponse;
import com.anpatapain.coffwok.security.utils.JwtUtils;
import com.anpatapain.coffwok.user.repository.UserRepository;
import com.anpatapain.coffwok.user.model.AuthProvider;
import com.anpatapain.coffwok.user.model.Role;
import com.anpatapain.coffwok.user.model.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JwtUtils jwtUtils;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signupRequest) {
        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email is already exist"));
        }

        User user = new User(
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        user.setProvider(AuthProvider.local);
        if(signupRequest.getRole() == null) {
            user.setRole(Role.ROLE_USER);
        }else {
            user.setRole(signupRequest.getRole());
        }
        User result = userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "Register successfully. Please login : )"));
    }
}
