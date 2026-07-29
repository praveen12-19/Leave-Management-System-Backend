package com.college.leavemanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_user_id", nullable = false)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private User recipientUser;

    @Column(nullable = false, length = 1000)
    private String message;

    private Long leaveApplicationId;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(Long id, User recipientUser, String message, Long leaveApplicationId, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.recipientUser = recipientUser;
        this.message = message;
        this.leaveApplicationId = leaveApplicationId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getRecipientUser() { return recipientUser; }
    public void setRecipientUser(User recipientUser) { this.recipientUser = recipientUser; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getLeaveApplicationId() { return leaveApplicationId; }
    public void setLeaveApplicationId(Long leaveApplicationId) { this.leaveApplicationId = leaveApplicationId; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class NotificationBuilder {
        private Long id;
        private User recipientUser;
        private String message;
        private Long leaveApplicationId;
        private boolean isRead;
        private LocalDateTime createdAt;

        NotificationBuilder() {}

        public NotificationBuilder id(Long id) { this.id = id; return this; }
        public NotificationBuilder recipientUser(User recipientUser) { this.recipientUser = recipientUser; return this; }
        public NotificationBuilder message(String message) { this.message = message; return this; }
        public NotificationBuilder leaveApplicationId(Long leaveApplicationId) { this.leaveApplicationId = leaveApplicationId; return this; }
        public NotificationBuilder isRead(boolean isRead) { this.isRead = isRead; return this; }
        public NotificationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Notification build() {
            return new Notification(id, recipientUser, message, leaveApplicationId, isRead, createdAt);
        }
    }
}
