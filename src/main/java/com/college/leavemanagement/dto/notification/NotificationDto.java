package com.college.leavemanagement.dto.notification;

import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    private Long recipientUserId;
    private String message;
    private Long leaveApplicationId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecipientUserId() { return recipientUserId; }
    public void setRecipientUserId(Long recipientUserId) { this.recipientUserId = recipientUserId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getLeaveApplicationId() { return leaveApplicationId; }
    public void setLeaveApplicationId(Long leaveApplicationId) { this.leaveApplicationId = leaveApplicationId; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
