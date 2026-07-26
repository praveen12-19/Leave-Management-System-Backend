package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.notification.NotificationDto;
import com.college.leavemanagement.entity.Notification;
import com.college.leavemanagement.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-26T15:21:35+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationDto toDto(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationDto notificationDto = new NotificationDto();

        notificationDto.setRecipientUserId( notificationRecipientUserId( notification ) );
        notificationDto.setId( notification.getId() );
        notificationDto.setMessage( notification.getMessage() );
        notificationDto.setLeaveApplicationId( notification.getLeaveApplicationId() );
        notificationDto.setRead( notification.isRead() );
        notificationDto.setCreatedAt( notification.getCreatedAt() );

        return notificationDto;
    }

    private Long notificationRecipientUserId(Notification notification) {
        User recipientUser = notification.getRecipientUser();
        if ( recipientUser == null ) {
            return null;
        }
        return recipientUser.getId();
    }
}
