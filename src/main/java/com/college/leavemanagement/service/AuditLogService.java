package com.college.leavemanagement.service;

import com.college.leavemanagement.entity.AuditLog;
import com.college.leavemanagement.repository.AuditLogRepository;
import com.college.leavemanagement.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String action, String entityType, Long entityId, String details) {
        Long actorUserId = null;
        String actorUsername = "SYSTEM";
        String actorRole = "SYSTEM";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            actorUserId = principal.getId();
            actorUsername = principal.getUsername();
            actorRole = principal.getRole().name();
        }

        AuditLog auditLog = AuditLog.builder()
                .actorUserId(actorUserId)
                .actorUsername(actorUsername)
                .actorRole(actorRole)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();

        auditLogRepository.save(auditLog);
        log.info("AUDIT LOG: [{}] by user '{}' ({}) on {} #{} - {}", 
                action, actorUsername, actorRole, entityType, entityId, details);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }
}
