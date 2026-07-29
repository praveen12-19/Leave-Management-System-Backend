package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.auth.JwtResponse;
import com.college.leavemanagement.dto.auth.LoginRequest;
import com.college.leavemanagement.entity.Hod;
import com.college.leavemanagement.entity.Mentor;
import com.college.leavemanagement.entity.Student;
import com.college.leavemanagement.entity.Warden;
import com.college.leavemanagement.enums.Role;
import com.college.leavemanagement.repository.HodRepository;
import com.college.leavemanagement.repository.MentorRepository;
import com.college.leavemanagement.repository.StudentRepository;
import com.college.leavemanagement.repository.WardenRepository;
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
    private final HodRepository hodRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final WardenRepository wardenRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       AuditLogService auditLogService,
                       HodRepository hodRepository,
                       StudentRepository studentRepository,
                       MentorRepository mentorRepository,
                       WardenRepository wardenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.auditLogService = auditLogService;
        this.hodRepository = hodRepository;
        this.studentRepository = studentRepository;
        this.mentorRepository = mentorRepository;
        this.wardenRepository = wardenRepository;
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

        String displayName = userPrincipal.getUsername();
        if (userPrincipal.getRole() == Role.HOD) {
            displayName = hodRepository.findByUserId(userPrincipal.getId()).map(Hod::getName).orElse(displayName);
        } else if (userPrincipal.getRole() == Role.STUDENT) {
            displayName = studentRepository.findByUserId(userPrincipal.getId()).map(Student::getName).orElse(displayName);
        } else if (userPrincipal.getRole() == Role.MENTOR) {
            displayName = mentorRepository.findByUserId(userPrincipal.getId()).map(Mentor::getName).orElse(displayName);
        } else if (userPrincipal.getRole() == Role.WARDEN) {
            displayName = wardenRepository.findByUserId(userPrincipal.getId()).map(Warden::getName).orElse(displayName);
        }

        if (displayName != null && !displayName.isEmpty()) {
            displayName = displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
        }

        return JwtResponse.builder()
                .token(jwt)
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .name(displayName)
                .email(userPrincipal.getEmail())
                .role(userPrincipal.getRole())
                .linkedProfileId(userPrincipal.getLinkedProfileId())
                .build();
    }
}
