package com.college.leavemanagement.config;

import com.college.leavemanagement.entity.User;
import com.college.leavemanagement.enums.Role;
import com.college.leavemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("DELETE FROM notifications");
            log.info("Purged stale notifications from database.");
        } catch (Exception e) {
            log.warn("Could not purge notifications: {}", e.getMessage());
        }

        try {
            jdbcTemplate.execute("ALTER TABLE notifications DROP FOREIGN KEY FKt8ievafor22iuvg5sd4p7lhbk");
            jdbcTemplate.execute("ALTER TABLE notifications ADD CONSTRAINT FKt8ievafor22iuvg5sd4p7lhbk FOREIGN KEY (recipient_user_id) REFERENCES users(id) ON DELETE CASCADE");
            log.info("Updated FKt8ievafor22iuvg5sd4p7lhbk with ON DELETE CASCADE in MySQL.");
        } catch (Exception e) {
            log.info("FK constraint alter notice: {}", e.getMessage());
        }

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
