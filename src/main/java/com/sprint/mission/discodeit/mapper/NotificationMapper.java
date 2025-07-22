package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import org.springframework.stereotype.Component;
import com.sprint.mission.discodeit.entity.Notification;

@Component
public class NotificationMapper {

    public NotificationDto toDto(Notification entity) {
        return new NotificationDto(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getReceiver().getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getType(),
                entity.getTargetId()
        );
    }

}