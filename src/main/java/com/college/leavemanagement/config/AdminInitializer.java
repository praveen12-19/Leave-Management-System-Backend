package com.college.leavemanagement.config;

import com.college.leavemanagement.entity.User;
import com.college.leavemanagement.enums.Role;
import com.college.leavemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository.findByUsername("admin").ifPresentOrElse(
            existingAdmin -> {
                existingAdmin.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(existingAdmin);
                log.info("UPDATED DEFAULT ADMIN ACCOUNT -> Username: 'admin' | Password: 'admin123'");
            },
            () -> {
                User adminUser = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(adminUser);
                log.info("SEEDED DEFAULT ADMIN ACCOUNT -> Username: 'admin' | Password: 'admin123'");
            }
        );
    }
}
