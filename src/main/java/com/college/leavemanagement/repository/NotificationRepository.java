package com.college.leavemanagement.repository;

import com.college.leavemanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientUserIdOrderByCreatedAtDesc(Long recipientUserId);
    long countByRecipientUserIdAndIsReadFalse(Long recipientUserId);

    @org.springframework.data.jpa.repository.Modifying(clearAutomatically = true, flushAutomatically = true)
    @org.springframework.data.jpa.repository.Query("DELETE FROM Notification n WHERE n.recipientUser.id = :userId")
    void deleteByRecipientUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

    @org.springframework.data.jpa.repository.Modifying(clearAutomatically = true, flushAutomatically = true)
    @org.springframework.data.jpa.repository.Query(value = "DELETE FROM notifications WHERE recipient_user_id = :userId", nativeQuery = true)
    int deleteNotificationsByUserIdNative(@org.springframework.data.repository.query.Param("userId") Long userId);
}
