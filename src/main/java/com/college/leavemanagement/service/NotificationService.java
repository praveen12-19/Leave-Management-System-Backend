package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.notification.NotificationDto;
import com.college.leavemanagement.entity.Notification;
import com.college.leavemanagement.entity.User;
import com.college.leavemanagement.exception.ResourceNotFoundException;
import com.college.leavemanagement.mapper.NotificationMapper;
import com.college.leavemanagement.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Transactional
    public Notification createNotification(User recipient, String message, Long leaveApplicationId) {
        log.info("Notifications disabled - skipping creation for user '{}'", recipient != null ? recipient.getUsername() : "null");
        return null;
    }

    public List<NotificationDto> getUserNotifications(Long recipientUserId) {
        return java.util.Collections.emptyList();
    }

    public long getUnreadCount(Long recipientUserId) {
        return 0L;
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        // No-op
    }
}
