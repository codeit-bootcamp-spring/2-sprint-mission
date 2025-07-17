package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDto toDto(Notification notification) {
        return new NotificationDto(
            notification.getId(),
            notification.getCreatedAt(),
            notification.getReceiverId(),
            notification.getTitle(),
            notification.getContent(),
            notification.getNotificationType(),
            notification.getTargetId()
        );
    }

}
