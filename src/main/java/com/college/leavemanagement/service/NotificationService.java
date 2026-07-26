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
        Notification notification = Notification.builder()
                .recipientUser(recipient)
                .message(message)
                .leaveApplicationId(leaveApplicationId)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        log.info("Created in-app notification for user '{}': {}", recipient.getUsername(), message);
        return notificationRepository.save(notification);
    }

    public List<NotificationDto> getUserNotifications(Long recipientUserId) {
        return notificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(recipientUserId)
                .stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(Long recipientUserId) {
        return notificationRepository.countByRecipientUserIdAndIsReadFalse(recipientUserId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
