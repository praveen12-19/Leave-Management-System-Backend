package com.college.leavemanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long actorUserId;
    private String actorUsername;
    private String actorRole;

    @Column(nullable = false)
    private String action;

    private String entityType;
    private Long entityId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 1000)
    private String details;

    public AuditLog() {}

    public AuditLog(Long id, Long actorUserId, String actorUsername, String actorRole, String action, String entityType, Long entityId, LocalDateTime timestamp, String details) {
        this.id = id;
        this.actorUserId = actorUserId;
        this.actorUsername = actorUsername;
        this.actorRole = actorRole;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.timestamp = timestamp;
        this.details = details;
    }

    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getActorUserId() { return actorUserId; }
    public void setActorUserId(Long actorUserId) { this.actorUserId = actorUserId; }

    public String getActorUsername() { return actorUsername; }
    public void setActorUsername(String actorUsername) { this.actorUsername = actorUsername; }

    public String getActorRole() { return actorRole; }
    public void setActorRole(String actorRole) { this.actorRole = actorRole; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public static class AuditLogBuilder {
        private Long id;
        private Long actorUserId;
        private String actorUsername;
        private String actorRole;
        private String action;
        private String entityType;
        private Long entityId;
        private LocalDateTime timestamp;
        private String details;

        AuditLogBuilder() {}

        public AuditLogBuilder id(Long id) { this.id = id; return this; }
        public AuditLogBuilder actorUserId(Long actorUserId) { this.actorUserId = actorUserId; return this; }
        public AuditLogBuilder actorUsername(String actorUsername) { this.actorUsername = actorUsername; return this; }
        public AuditLogBuilder actorRole(String actorRole) { this.actorRole = actorRole; return this; }
        public AuditLogBuilder action(String action) { this.action = action; return this; }
        public AuditLogBuilder entityType(String entityType) { this.entityType = entityType; return this; }
        public AuditLogBuilder entityId(Long entityId) { this.entityId = entityId; return this; }
        public AuditLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AuditLogBuilder details(String details) { this.details = details; return this; }

        public AuditLog build() {
            return new AuditLog(id, actorUserId, actorUsername, actorRole, action, entityType, entityId, timestamp, details);
        }
    }
}
