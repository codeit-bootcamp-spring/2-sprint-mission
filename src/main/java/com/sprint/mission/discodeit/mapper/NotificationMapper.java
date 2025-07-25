package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import org.springframework.stereotype.Component;
import com.sprint.mission.discodeit.entity.Notification;

@Component
public class NotificationMapper {

    public NotificationDto toDto(Notification entity) {
        NotificationDto dto = new NotificationDto();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setReceiverId(entity.getReceiver().getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setType(entity.getType());
        dto.setTargetId(entity.getTargetId());
        return dto;
    }
}