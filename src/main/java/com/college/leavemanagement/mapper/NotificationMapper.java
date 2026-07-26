package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.notification.NotificationDto;
import com.college.leavemanagement.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "recipientUser.id", target = "recipientUserId")
    NotificationDto toDto(Notification notification);
}
