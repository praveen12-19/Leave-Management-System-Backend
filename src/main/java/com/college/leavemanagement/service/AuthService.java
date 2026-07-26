package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.auth.JwtResponse;
import com.college.leavemanagement.dto.auth.LoginRequest;
import com.college.leavemanagement.security.JwtUtil;
import com.college.leavemanagement.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuditLogService auditLogService;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuditLogService auditLogService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.auditLogService = auditLogService;
    }

    public JwtResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for user: {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        auditLogService.logAction("LOGIN_SUCCESS", "User", userPrincipal.getId(), 
                "User successfully logged in with role: " + userPrincipal.getRole());

        return JwtResponse.builder()
                .token(jwt)
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .email(userPrincipal.getEmail())
                .role(userPrincipal.getRole())
                .linkedProfileId(userPrincipal.getLinkedProfileId())
                .build();
    }
}
